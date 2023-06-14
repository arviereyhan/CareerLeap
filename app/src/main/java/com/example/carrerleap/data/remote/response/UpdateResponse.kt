package com.example.carrerleap.data.remote.response

import com.google.gson.annotations.SerializedName

data class UpdateResponse(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)
