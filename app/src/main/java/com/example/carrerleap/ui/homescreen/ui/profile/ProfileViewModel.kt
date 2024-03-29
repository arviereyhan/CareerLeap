package com.example.carrerleap.ui.homescreen.ui.profile

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.carrerleap.data.repository.DataRepository

class ProfileViewModel(private val repository: DataRepository) : ViewModel() {
    fun get_profile(token: String) = repository.getProfile(token)
    fun getJobs(token: String) = repository.getJobs(token)
}