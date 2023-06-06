package com.example.carrerleap.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.carrerleap.data.remote.network.ApiService
import com.example.carrerleap.data.remote.response.LoginResponse
import com.example.carrerleap.data.remote.response.RegisterResponse
import com.example.carrerleap.utils.Result
import okhttp3.MultipartBody

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

    fun uploadCv(file: MultipartBody.Part, token: String): LiveData<Result<RegisterResponse>> = liveData {
        try {
            val bearerToken = generateBearerToken(token)
            val response = apiService.uploadCv(file,bearerToken)
            emit(Result.Success(response))
        } catch (e:Exception){
            emit(Result.Error(e.toString()))
        }
    }

    private fun generateBearerToken(token: String): String {
        return "Bearer $token"
    }



}