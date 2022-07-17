package com.example.notesapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.notesapp.api.NotesAPI
import com.example.notesapp.model.NoteRequest
import com.example.notesapp.model.NoteResponse
import com.example.notesapp.utils.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class NotesRepository @Inject constructor(private val notesAPI: NotesAPI){

    private val _notesLiveData=MutableLiveData<NetworkResult<List<NoteResponse>>>()
    val notesLiveData:LiveData<NetworkResult<List<NoteResponse>>>
    get()=_notesLiveData!!

    private val _statusLiveData=MutableLiveData<NetworkResult<String>>()
    val statusLiveData:LiveData<NetworkResult<String>>
    get() =_statusLiveData

    suspend fun getNotes(){
        _notesLiveData.postValue(NetworkResult.Loading())
        val response=notesAPI.getNotes()
        handleResponse(response);
    }

    suspend fun createNotes(noteRequest: NoteRequest){
        _notesLiveData.postValue(NetworkResult.Loading())
        val response=notesAPI.createNotes(noteRequest)
        if (response.isSuccessful && response.body()!=null){
            _statusLiveData.postValue(NetworkResult.Success("Note Created"))
        }else{
            _statusLiveData.postValue(NetworkResult.Success("Something went wrong"))
        }
    }

    suspend fun updateNotes(noteId:String,noteRequest: NoteRequest){
        _notesLiveData.postValue(NetworkResult.Loading())
        val response=notesAPI.updateNote(noteId,noteRequest)
        if (response.isSuccessful && response.body()!=null){
            _statusLiveData.postValue(NetworkResult.Success("Note Update"))
        }else{
            _statusLiveData.postValue(NetworkResult.Success("Something went wrong"))
        }
    }

    suspend fun deleteNotes(noteId: String){
        _notesLiveData.postValue(NetworkResult.Loading())
        val response=notesAPI.deleteNotes(noteId)
        if (response.isSuccessful && response.body()!=null){
            _statusLiveData.postValue(NetworkResult.Success("Note Deleted"))
        }else{
            _statusLiveData.postValue(NetworkResult.Success("Something went wrong"))
        }
    }


    private fun handleResponse(response: Response<List<NoteResponse>>) {
        if (response.isSuccessful && response.body()!=null){
            _notesLiveData.postValue(NetworkResult.Success(response.body()!!))
        }else if(response.errorBody()!=null){
            val errormsg=JSONObject(response.errorBody()!!.charStream().readText())
            _notesLiveData.postValue(NetworkResult.Error(errormsg.getString("message")))
        }else{
            _notesLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }

}