package com.alilopez.kt_demohilt.features.HireBeat.data.datasources.remote.models

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class SimpleCatalogResponseDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
)