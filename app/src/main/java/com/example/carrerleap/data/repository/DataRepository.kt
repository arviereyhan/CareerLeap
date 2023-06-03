package com.example.carrerleap.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.carrerleap.data.remote.network.ApiService
import com.example.carrerleap.data.remote.response.LoginResponse
import com.example.carrerleap.data.remote.response.RegisterResponse
import com.example.carrerleap.utils.Result

class DataRepository(private val apiService: ApiService) {

    fun register(name: String, email: String, password: String): LiveData<Result<RegisterResponse>> = liveData{
        try {
            val response = apiService.register(name, email, password)
            emit(Result.Success(response))
        }
        catch(e: Exception){
            emit(Result.Error(e.toString()))
        }
    }

    fun login(email: String, password: String): LiveData<Result<LoginResponse>> = liveData{
        try {
            val response = apiService.login(email, password)
            emit(Result.Success(response))
        }
        catch(e: Exception){
            emit(Result.Error(e.toString()))
        }
    }



}