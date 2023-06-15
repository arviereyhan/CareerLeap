package com.example.carrerleap.utils

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ListHomeItem(
    val id: Int,
    val skill: String,
): Parcelable