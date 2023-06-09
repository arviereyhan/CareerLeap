package com.example.carrerleap.utils

data class Job(
    val id: Int,
    val jobName: String
)

data class Question(
    val id: Int,
    val question: String,
    val jobId: Int
)