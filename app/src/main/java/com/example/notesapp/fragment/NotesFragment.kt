package com.example.notesapp.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.notesapp.R
import com.example.notesapp.databinding.FragmentNotesBinding
import com.example.notesapp.model.NoteRequest
import com.example.notesapp.model.NoteResponse
import com.example.notesapp.utils.NetworkResult
import com.example.notesapp.viewModel.NotesViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotesFragment : Fragment() {


    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!
    private var note: NoteResponse? = null
    private val noteViewModel by viewModels<NotesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setInitiateData()
        bindHandelars()
        bindObserver()
    }

    private fun bindObserver() {
        noteViewModel.notesLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible=false
            when (it) {
                is NetworkResult.Success -> {
                    findNavController().popBackStack()
                    Log.d("TAG", "popup"+it.data)
                }
                is NetworkResult.Error -> {}
                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible=true
                }
            }
        })

        noteViewModel.statusLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible=false
            when (it) {
                is NetworkResult.Success -> {
                    Toast.makeText(context,it.data,Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
                is NetworkResult.Error -> {}
                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible=true
                }
            }
        })
    }

    private fun bindHandelars() {
        binding.btnDelete.setOnClickListener {
            note?.let {
                noteViewModel.deleteNotes(it._id)
            }
        }

        binding.btnSubmit.setOnClickListener {
            val title=binding.txtTitle.text.toString()
            val desc=binding.txtDescription.text.toString()
            val noteRequest=NoteRequest(title,desc)
            if (note==null){
                noteViewModel.createNotes(noteRequest)
            }else{
                noteViewModel.updateNotes(note!!._id,noteRequest)
            }

        }
    }

    private fun setInitiateData() {
        val jsonNote = arguments?.getString("note")
        if (jsonNote != null) {
            note = Gson().fromJson(jsonNote, NoteResponse::class.java)

            note?.let {
                binding.txtTitle.setText(it.title)
                binding.txtDescription.setText(it.description)
            }

        } else {
            binding.addEditText.text = "Add Note"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}