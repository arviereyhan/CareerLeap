package com.example.carrerleap.ui.auth.login

import androidx.lifecycle.ViewModel
import com.example.carrerleap.data.repository.DataRepository

class LoginViewModel(private val repository: DataRepository): ViewModel() {
    fun postLogin(email: String, password: String) = repository.login(email, password)
    fun getProfile(token: String) = repository.getProfile(token)

    fun getScore(token: String) = repository.getHome(token)
}