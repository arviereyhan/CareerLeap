package com.example.carrerleap.data.remote.response

import com.google.gson.annotations.SerializedName

data class PredictJobsResponse(
	@field:SerializedName("hasil_prediksi")
	val hasilPrediksi: String,

	@field:SerializedName("text_output")
	val textOutput: String
)
