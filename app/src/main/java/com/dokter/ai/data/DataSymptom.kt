package com.dokter.ai.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataSymptom(
    val id: String,
    val name: String,
    val desc: String,
    val img: String,
    val question: String,
    var selected: Boolean
): Parcelable