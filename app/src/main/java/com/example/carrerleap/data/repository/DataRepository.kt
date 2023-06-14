package com.example.carrerleap.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.carrerleap.data.remote.network.ApiService
import com.example.carrerleap.data.remote.response.LoginResponse
import com.example.carrerleap.data.remote.response.ProfileResponse
import com.example.carrerleap.data.remote.response.RegisterResponse
import com.example.carrerleap.data.remote.response.UpdateResponse
import com.example.carrerleap.utils.Result
import okhttp3.MultipartBody
import okhttp3.RequestBody

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

    fun getProfile(token : String): LiveData<Result<ProfileResponse>> = liveData{
        try {
            Log.d("TOKEN", token)
            val response = apiService.get_profile("Bearer $token")
            emit(Result.Success(response))
        }
        catch(e: Exception){
            emit(Result.Error(e.toString()))
        }
    }

    fun updateProfile(token: String,full_name: RequestBody,date_of_birth: RequestBody, phonenumber: RequestBody, location: RequestBody,image:MultipartBody.Part): LiveData<Result<UpdateResponse>> = liveData {
        try {
            val response = apiService.updateProfile("Bearer $token",full_name,date_of_birth,phonenumber,location,image)
            emit(Result.Success(response))
        }
        catch(e: Exception){
            emit(Result.Error(e.toString()))
        }
    }


}