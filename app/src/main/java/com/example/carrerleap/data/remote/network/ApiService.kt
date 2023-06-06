package com.example.carrerleap.data.remote.network

import com.example.carrerleap.data.remote.response.LoginResponse
import com.example.carrerleap.data.remote.response.ProfileResponse
import com.example.carrerleap.data.remote.response.RegisterResponse
import retrofit2.http.*

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

    @GET("profile")
    suspend fun get_profile(
        @Header("Authorization") authorization: String
    ): ProfileResponse

}