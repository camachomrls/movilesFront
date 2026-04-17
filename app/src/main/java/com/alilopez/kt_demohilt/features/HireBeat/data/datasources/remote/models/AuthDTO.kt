package com.alilopez.kt_demohilt.features.HireBeat.data.datasources.remote.models

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestDto(
    val email: String,
    val password: String
)

@Serializable
data class RegisterRequestDto(
    val fullname: String,
    val email: String,
    val password: String,
    @SerializedName("roleId")
    val roleId: String,

    @SerializedName("roleName")
    val roleName: String,
    )

@Serializable
data class AuthResponseDto(
    val token: String = ""
)
