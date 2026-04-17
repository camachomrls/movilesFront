package com.alilopez.kt_demohilt.features.HireBeat.data.datasources.remote.models

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileSetupRequestDto(
    @SerializedName("city") val city: String,
    @SerializedName("experience") val experience: Int,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("genres") val genres: List<Int>,
    @SerializedName("instruments") val instruments: List<InstrumentSelectionRequestDto>,
    @SerializedName("links") val links: List<PlataformasProfileRequestDto>
)

@Serializable
data class InstrumentSelectionRequestDto(
    @SerializedName("instrumentId") val instrumentId: Int,
    @SerializedName("level") val level: Int,
    @SerializedName("isPrincipal") val isPrincipal: Boolean
)

@Serializable
data class PlataformasProfileRequestDto(
    @SerializedName("name") val name: String,
    @SerializedName("type") val type: Int,
    @SerializedName("ref") val ref: String
)