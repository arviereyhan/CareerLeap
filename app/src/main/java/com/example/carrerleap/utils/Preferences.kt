package com.example.carrerleap.utils

import android.content.Context

internal class Preferences(context: Context) {
    companion object{
        private const val PREFS_NAME = "pref"
        private const val TOKEN = "token"
        private const val NAME = "name"
        private const val USER_ID = "userId"
    }
    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)


    fun saveToken(data: UserModel){
        val editor = preferences.edit()
        editor.putString(TOKEN, data.token)
        editor.apply()
    }

    fun getToken(): UserModel {
        val token = preferences.getString(TOKEN, null)
        return UserModel(token)
    }

    fun logout() {
        val editor = preferences.edit().clear()
        editor.apply()
    }


}