package com.example.carrerleap.ui.homescreen.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.carrerleap.data.repository.DataRepository

class HomeViewModel(private val repository: DataRepository) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    fun getProfile(token: String) = repository.getProfile(token)
    fun getJobs(token: String) = repository.getJobs(token)

    fun getHome(token: String) = repository.getHome(token)
    fun getQuestion(token: String) = repository.getQuestions(token)
}