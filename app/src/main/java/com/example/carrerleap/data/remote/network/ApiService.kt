package com.example.carrerleap.data.remote.network

import com.example.carrerleap.data.remote.response.RegisterResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    @POST("register")
    suspend fun register(
        @Field("name") name : String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

}