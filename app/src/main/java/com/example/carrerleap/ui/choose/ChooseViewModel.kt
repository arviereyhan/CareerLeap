package com.example.carrerleap.ui.choose

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.carrerleap.data.repository.DataRepository

class ChooseViewModel(private val repository: DataRepository): ViewModel() {
    fun getJobs(token: String) = repository.getJobs(token)

    fun postJobs(id: Int, token:String) = repository.postJobs(id, token)
    fun getProfile(token: String) = repository.getProfile(token)
}