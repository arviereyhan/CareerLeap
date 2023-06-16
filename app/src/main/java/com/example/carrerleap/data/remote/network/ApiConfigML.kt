package com.example.carrerleap.data.remote.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiConfigML {
    companion object{
        fun getApiService(): ApiServiceML {
            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS) // Mengatur waktu tunggu koneksi menjadi 30 detik
                .readTimeout(60, TimeUnit.SECONDS) // Mengatur waktu tunggu baca menjadi 30 detik
                .writeTimeout(60, TimeUnit.SECONDS) // Mengatur waktu tunggu tulis menjadi 30 detik
                .addInterceptor(loggingInterceptor)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://careerleap-api-ml-6hpk63vaeq-as.a.run.app/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiServiceML::class.java)
        }
    }

}