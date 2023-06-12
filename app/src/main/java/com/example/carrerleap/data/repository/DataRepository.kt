package com.example.carrerleap.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.carrerleap.data.remote.network.ApiService
import com.example.carrerleap.data.remote.response.HomeResponse
import com.example.carrerleap.data.remote.response.JobsResponse
import com.example.carrerleap.data.remote.response.LoginResponse
import com.example.carrerleap.data.remote.response.ProfileResponse
import com.example.carrerleap.data.remote.response.QuestionsResponse
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

    fun getJobs(token : String): LiveData<Result<JobsResponse>> = liveData{
        try {
            Log.d("TOKEN", token)
            val response = apiService.getJobs("Bearer $token")
            emit(Result.Success(response))
        }
        catch(e: Exception){
            emit(Result.Error(e.toString()))
        }
    }

    fun postJobs(id : Int,token : String): LiveData<Result<RegisterResponse>> = liveData{
        try {
            Log.d("TOKEN", token)
            val response = apiService.
            postJobs(id, "Bearer $token")
            emit(Result.Success(response))
        }
        catch(e: Exception){
            emit(Result.Error(e.toString()))
        }
    }

    fun postScore(id : Int,score : Int,token : String): LiveData<Result<RegisterResponse>> = liveData{
        try {
            Log.d("TOKEN", token)
            val response = apiService.postScore(id, score, "Bearer $token")
            emit(Result.Success(response))
        }
        catch(e: Exception){
            emit(Result.Error(e.toString()))
        }
    }



    fun getQuestions(token : String): LiveData<Result<QuestionsResponse>> = liveData{
        try {
            Log.d("TOKEN", token)
            val response = apiService.getQuestions("Bearer $token")
            emit(Result.Success(response))
        }
        catch(e: Exception){
            emit(Result.Error(e.toString()))
        }
    }

    fun getHome(token : String): LiveData<Result<HomeResponse>> = liveData{
        try {
            Log.d("TOKEN", token)
            val response = apiService.getHome("Bearer $token")
            emit(Result.Success(response))
        }
        catch(e: Exception){
            emit(Result.Error(e.toString()))
        }
    }

    private fun generateBearerToken(token: String): String {
        return "Bearer $token"
    }



}