package com.alilopez.kt_demohilt.features.HireBeat.data.datasources.remote.models

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileResponseDto(
    @SerializedName("id") val id: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("fullname") val fullname: String? = null,
    @SerializedName("city") val city: String? = null,
    @SerializedName("experience") val experience: Int? = null,
    @SerializedName("descripcion") val descripcion: String? = null,
    @SerializedName("genres") val genres: List<GenreResponseDto>? = emptyList(),
    @SerializedName("instruments") val instruments: List<InstrumentResponseDto>? = emptyList(),
    @SerializedName("links") val links: List<LinkResponseDto>? = emptyList()
)

@Serializable
data class GenreResponseDto(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("name") val name: String? = null
)

@Serializable
data class InstrumentResponseDto(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("level") val level: String? = null,
    @SerializedName("isPrincipal") val isPrincipal: Boolean? = false
)

@Serializable
data class LinkResponseDto(
    @SerializedName("name") val name: String? = null,
    @SerializedName("type") val type: Int? = null,
    @SerializedName("ref") val ref: String? = null
)