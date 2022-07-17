package com.example.notesapp.api

import com.example.notesapp.utils.SharedPreference
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor() : Interceptor {

    @Inject
    lateinit var sharedPreference: SharedPreference

    override fun intercept(chain: Interceptor.Chain): Response {
        val request=chain.request().newBuilder()
        val token=sharedPreference.getToken()
        request.addHeader("Authorization","Bearer $token")
        return chain.proceed(request.build())
    }
}