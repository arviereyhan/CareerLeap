package com.example.carrerleap.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class HomeResponse(

	@field:SerializedName("data")
	val data: List<HomeItem?>? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

@Parcelize
data class HomeItem(

	@field:SerializedName("score")
	val score: Int? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("question_id")
	val questionId: Int? = null
):Parcelable
