package com.example.notesapp.api

import com.example.notesapp.model.NoteRequest
import com.example.notesapp.model.NoteResponse
import retrofit2.Response
import retrofit2.http.*

interface NotesAPI {

    @GET("/note")
    suspend fun getNotes():Response<List<NoteResponse>>

    @POST("/note")
    suspend fun createNotes(@Body noteRequest: NoteRequest):Response<NoteResponse>

    @PUT("/note/{noteId}")
    suspend fun updateNote(@Path("noteId") noteId:String,@Body noteRequest: NoteRequest):Response<NoteResponse>

    @DELETE("/note/{noteId}")
    suspend fun deleteNotes(@Path("noteId") noteId: String):Response<NoteResponse>

}