package com.example.carrerleap.utils

import android.content.Context
import com.example.carrerleap.data.remote.network.ApiConfig
import com.example.carrerleap.data.repository.DataRepository

object Injection {
    fun provideRepository(context: Context): DataRepository {
        val apiService = ApiConfig.getApiService()
        return DataRepository(apiService)
    }
}