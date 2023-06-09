package com.example.carrerleap.data.remote.response

import com.google.gson.annotations.SerializedName

data class QuestionsResponse(

	@field:SerializedName("data")
	val data: List<DataItem?>?,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class DataItem(

	@field:SerializedName("question")
	val question: String? = null,

	@field:SerializedName("job_id")
	val jobId: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null
)
