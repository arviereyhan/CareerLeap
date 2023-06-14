package com.example.carrerleap.data.remote.network

import com.example.carrerleap.data.remote.response.LoginResponse
import com.example.carrerleap.data.remote.response.ProfileResponse
import com.example.carrerleap.data.remote.response.RegisterResponse
import com.example.carrerleap.data.remote.response.UpdateResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
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
        @Header("Authorization") token: String
    ): ProfileResponse

    @PUT("/profile")
    @Multipart
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Part("full_name") fullName: RequestBody,
        @Part("date_of_birth") dateOfBirth: RequestBody,
        @Part("phone_number") phoneNumber: RequestBody,
        @Part("location") location: RequestBody,
        @Part file_profile: MultipartBody.Part,
    ): UpdateResponse

}