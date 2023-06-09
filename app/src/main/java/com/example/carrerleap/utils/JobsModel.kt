package com.example.carrerleap.utils

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.ArrayList

@Parcelize
data class JobsModel(
    var jobsId: Int? = null,
    var score: IntArray? = null,
): Parcelable