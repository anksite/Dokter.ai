package com.dokter.ai.data.local.entity

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dokter.ai.data.network.ResponseDisease
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class EntityHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val date: String,
    val symptom: String,
    val disease: String,
    val accuracy: Int,
    val img: String,
    val diseaseDesc: String,
    val recom: String
):Parcelable
