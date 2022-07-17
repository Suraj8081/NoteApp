package com.example.notesapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapp.model.NoteRequest
import com.example.notesapp.model.NoteResponse
import com.example.notesapp.repository.NotesRepository
import com.example.notesapp.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(private val notesRepository: NotesRepository) : ViewModel() {

    val notesLiveData:LiveData<NetworkResult<List<NoteResponse>>>
    get() =notesRepository.notesLiveData
    val statusLiveData get()=notesRepository.statusLiveData

    fun getNotes(){
        viewModelScope.launch {
            notesRepository.getNotes()
        }
    }

    fun createNotes(notesRequest: NoteRequest){
        viewModelScope.launch {
            notesRepository.createNotes(notesRequest)
        }
    }

    fun updateNotes(noteId:String,notesRequest: NoteRequest){
        viewModelScope.launch {
            notesRepository.updateNotes(noteId,notesRequest)
        }
    }

    fun deleteNotes(noteId: String){
        viewModelScope.launch {
            notesRepository.deleteNotes(noteId)
        }
    }

}