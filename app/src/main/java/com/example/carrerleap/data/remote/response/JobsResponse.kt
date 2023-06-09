package com.example.carrerleap.data.remote.response

import com.google.gson.annotations.SerializedName

data class JobsResponse(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val data: List<JobsResult>
)

data class JobsResult (

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("job_name")
    val job_name: String,
)