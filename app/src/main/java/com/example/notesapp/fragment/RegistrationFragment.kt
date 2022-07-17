package com.example.notesapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.notesapp.R
import com.example.notesapp.databinding.FragmentRegistrationBinding
import com.example.notesapp.model.UserRequest
import com.example.notesapp.utils.NetworkResult
import com.example.notesapp.utils.SharedPreference
import com.example.notesapp.viewModel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegistrationFragment : Fragment() {


    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!
    private val authViewModel by viewModels<AuthViewModel>()

    @Inject
    lateinit var sharedPreference: SharedPreference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)

        if(sharedPreference.getToken()!=null){
            findNavController().navigate(R.id.action_registrationFragment_to_mainFragment)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            /*authViewModel.loginUser(
                UserRequest("test@hmail.com", "12345", "test")
            )*/
            findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
        }

        binding.btnSignUp.setOnClickListener {
            val userRequest = getUserRequest();
            val validateUser = authViewModel.validateUser(
                userRequest.email,
                userRequest.password,
                userRequest.username,
                false
            )
            if (validateUser.first) {
                authViewModel.registerUser(userRequest)
            } else {
                binding.txtError.text=validateUser.second
            }
        }

        bindObserver();
    }

    fun getUserRequest(): UserRequest {
        val email = binding.txtEmail.text.toString()
        val password = binding.txtPassword.text.toString()
        val username = binding.txtUsername.text.toString()
        return UserRequest(email, password, username)
    }


    private fun bindObserver() {
        authViewModel.userResponseLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    sharedPreference.setToken(it.data!!.token)
                    findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
                }
                is NetworkResult.Error -> {
                    binding.txtError.text = it.message
                }
                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}