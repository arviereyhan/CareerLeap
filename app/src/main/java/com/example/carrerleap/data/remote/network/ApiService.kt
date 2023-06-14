package com.example.carrerleap.data.remote.network

import com.example.carrerleap.data.remote.response.CourseResponse
import com.example.carrerleap.data.remote.response.HomeResponse
import com.example.carrerleap.data.remote.response.JobsResponse
import com.example.carrerleap.data.remote.response.LoginResponse
import com.example.carrerleap.data.remote.response.ProfileResponse
import com.example.carrerleap.data.remote.response.QuestionsResponse
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
        @Header("Authorization") authorization: String
    ): ProfileResponse

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

    @GET("getCourses")
    suspend fun getCourse(
        @Query ("questionId") questionId: Int,
        @Query ("userScore") userScore: Int,
        @Query ("jobId") jobId: Int,
        @Header("Authorization") token: String
    ): CourseResponse

}