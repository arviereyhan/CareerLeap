package com.example.carrerleap.data.remote.response

import com.google.gson.annotations.SerializedName

data class PredictResponse(
    @field:SerializedName("hasil_prediksi")
    val hasilPrediksi: String? = null,

    @field:SerializedName("text_output")
    val textOutput: String? = null,
)
