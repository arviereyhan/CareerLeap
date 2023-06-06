package com.example.carrerleap.data.remote.network

import com.example.carrerleap.data.remote.response.LoginResponse
import com.example.carrerleap.data.remote.response.RegisterResponse
import okhttp3.MultipartBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import java.io.File

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("full_name") name : String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @FormUrlEncoded
    @PUT("cv")
    suspend fun uploadCv(
        @Field ("file_cv")file_cv : MultipartBody.Part,
        @Header("Authorization") token: String
    ): RegisterResponse

}