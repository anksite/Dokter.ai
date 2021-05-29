package com.dokter.ai.data

import com.dokter.ai.data.network.ResponseSymptom

object DataMapper {
    fun mapResponseToDomain(input: List<ResponseSymptom>): List<DataSymptom> = input.map {
        DataSymptom(
            it.id,
            it.name,
            it.description,
            it.image,
            it.question,
            false
        )
    }
}