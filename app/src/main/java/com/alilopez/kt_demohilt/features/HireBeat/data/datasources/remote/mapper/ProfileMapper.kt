package com.alilopez.kt_demohilt.features.HireBeat.data.datasources.remote.mapper

import com.alilopez.kt_demohilt.features.HireBeat.data.datasources.remote.models.ProfileResponseDto
import com.alilopez.kt_demohilt.features.HireBeat.domain.entities.UserProfile

fun ProfileResponseDto.toDomain(): UserProfile {
    val isMusician = !instruments.isNullOrEmpty()

    return UserProfile(
        id = this.id ?: "",
        fullName = this.fullname ?: "Usuario",
        description = this.descripcion?.takeIf { it.isNotBlank() } ?: "Sin descripción todavía...",
        role = if (isMusician) "Musician" else "Recruiter"
    )
}