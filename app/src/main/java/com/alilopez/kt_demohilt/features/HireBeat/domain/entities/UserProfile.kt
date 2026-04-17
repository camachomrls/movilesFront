package com.alilopez.kt_demohilt.features.HireBeat.domain.entities

data class UserProfile(
    val id: String,
    val fullName: String,
    val description: String,
    val role: String // "Musician" o "Recruiter"
)