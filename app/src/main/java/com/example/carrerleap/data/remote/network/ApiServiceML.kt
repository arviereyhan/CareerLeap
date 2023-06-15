package com.example.carrerleap.data.remote.network

import com.example.carrerleap.data.remote.response.PredictResponse
import com.example.carrerleap.data.remote.response.RegisterResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiServiceML {

    @FormUrlEncoded
    @POST("predict")
    suspend fun getPredict(
        @Field("cv_url") cvUrl : String,
    ): PredictResponse
}