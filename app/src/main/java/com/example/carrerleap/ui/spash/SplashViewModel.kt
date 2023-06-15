package com.example.carrerleap.ui.spash

import androidx.lifecycle.ViewModel
import com.example.carrerleap.data.repository.DataRepository

class SplashViewModel(private val repository: DataRepository): ViewModel() {
    fun getProfile (token: String) = repository.getProfile(token)

    fun getScore (token: String) = repository.getHome(token)
}