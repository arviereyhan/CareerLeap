package com.example.carrerleap.data.userdata

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserData(
    val name: String?,
    val profileurl: String?,
    val birthdate: String?,
    val phonenumber: String?,
    val email: String?,
    val location: String?
    ): Parcelable
