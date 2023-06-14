package com.example.carrerleap.data.userdata

data class UpdateProfileRequest(
    val full_name: String,
    val date_of_birth: String,
    val phone_number: String,
    val location: String
)
