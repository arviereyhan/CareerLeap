package com.example.carrerleap.ui.auth.register

import androidx.lifecycle.ViewModel
import com.example.carrerleap.data.repository.DataRepository

class RegisterViewModel(private val repository: DataRepository): ViewModel() {
    fun postRegister(name: String, email : String, password: String)= repository.register(name, email, password)
}