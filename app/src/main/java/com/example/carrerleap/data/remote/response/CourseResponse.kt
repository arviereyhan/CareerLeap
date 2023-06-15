package com.example.carrerleap.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class CourseResponse(

	@field:SerializedName("data")
	val courseItem: List<CourseItem>,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)

@Parcelize
data class CourseItem(

	@field:SerializedName("job_id")
	val jobId: Int,

	@field:SerializedName("course_link")
	val courseLink: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("course_score")
	val courseScore: Int,

	@field:SerializedName("question_id")
	val questionId: Int,

	@field:SerializedName("course_description")
	val courseDescription: String,

	@field:SerializedName("course_title")
	val courseTitle: String
): Parcelable
