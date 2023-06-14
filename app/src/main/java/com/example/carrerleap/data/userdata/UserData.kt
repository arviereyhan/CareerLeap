package com.example.carrerleap.data.userdata

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class UserData(
    var name: String?,
    var profileurl: String?,
    var birthdate: String?,
    var phonenumber: String?,
    var email: String?,
    var location: String?,
    var job: String?
): Parcelable
