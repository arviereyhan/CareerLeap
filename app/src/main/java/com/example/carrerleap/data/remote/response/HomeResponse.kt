package com.example.carrerleap.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class HomeResponse(

	@field:SerializedName("data")
	val data: List<HomeItem>,

	@field:SerializedName("error")
	val error: Boolean ,

	@field:SerializedName("message")
	val message: String
)

@Parcelize
data class HomeItem(

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("score")
	val score: Int,

	@field:SerializedName("question_id")
	val questionId: Int
):Parcelable
