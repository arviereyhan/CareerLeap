package com.example.carrerleap.data.remote.network

import com.example.carrerleap.data.remote.response.HomeResponse
import com.example.carrerleap.data.remote.response.JobsResponse
import com.example.carrerleap.data.remote.response.LoginResponse
import com.example.carrerleap.data.remote.response.ProfileResponse
import com.example.carrerleap.data.remote.response.QuestionsResponse
import com.example.carrerleap.data.remote.response.RegisterResponse
import okhttp3.RequestBody
import okhttp3.MultipartBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import java.io.File
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

    @Multipart
    @PUT("cv")
    suspend fun uploadCv(
        @Part file_cv : MultipartBody.Part,
        @Header("Authorization") token: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("insertJob")
    suspend fun postJobs(
        @Field ("job_id")jobs_id : Int,
        @Header("Authorization") token: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("insertScore")
    suspend fun postScore(
        @Field ("question_id")question_id : Int,
        @Field ("score")score : Int,
        @Header("Authorization") token: String
    ): RegisterResponse

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
    @GET("jobs")
    suspend fun getJobs(
        @Header("Authorization") token: String
    ): JobsResponse

    @GET("questions")
    suspend fun getQuestions(
        @Header("Authorization") token: String
    ): QuestionsResponse

    @GET("getScore")
    suspend fun getHome(
        @Header("Authorization") token: String
    ): HomeResponse

}