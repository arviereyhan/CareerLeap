package com.example.carrerleap.utils

import android.content.Context
import com.google.gson.Gson
import okhttp3.MultipartBody

internal class Preferences(context: Context) {
    companion object{
        private const val PREFS_NAME = "pref"
        private const val TOKEN = "token"
        private const val JOBS = "jobs"
        private const val SCORE = "score"
        private const val FILE = "file"
    }
    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)


    fun saveToken(data: UserModel){
        val editor = preferences.edit()
        editor.putString(TOKEN, data.token)
        editor.apply()
    }

    fun saveFile(data: CvModel){
        val editor = preferences.edit()
        editor.putString(FILE, data.fileCv)
        editor.apply()
    }

    fun saveJobs(data: JobsModel) {
        val editor = preferences.edit()
        editor.putString(JOBS, data.jobs)
        val scoreString = data.score?.joinToString(",") // Mengonversi IntArray menjadi String dengan delimiter koma
        editor.putString(SCORE, scoreString)
        editor.apply()
    }



    fun getJobs(): JobsModel {
        val jobs = preferences.getString(JOBS, null)
        val scoreString = preferences.getString(SCORE, null)
        val scoreIntArray = scoreString?.split(",")?.map { it.toIntOrNull() ?: 0 }?.toIntArray()
        return JobsModel(jobs, scoreIntArray)
    }

    fun getFile(): CvModel {
        val fileCv = preferences.getString(FILE, null)
        return CvModel(fileCv)
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

data class CvModel(
    var fileCv : String? = null
)