package com.example.notesapp.utils

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SharedPreference @Inject constructor(@ApplicationContext context: Context) {

    private val pref =
        context.getSharedPreferences(Constants.PREFS_TOKEN_FILE, Context.MODE_PRIVATE)


    fun setToken(token: String) {
        pref.edit()
            .putString(Constants.USER_TOKEN, token)
            .apply()
    }

    fun getToken(): String? {
        return pref.getString(Constants.USER_TOKEN, null)
    }

}