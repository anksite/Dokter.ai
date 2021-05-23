package com.dokter.ai.view

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataSymptom(
    val id: Int,
    val name: String,
    val desc: String,
    val img: String,
    var selected: Boolean
): Parcelable