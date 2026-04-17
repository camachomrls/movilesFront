package com.alilopez.kt_demohilt.features.HireBeat.data.datasources.remote.mapper

import com.alilopez.kt_demohilt.features.HireBeat.data.datasources.remote.models.AuthResponseDto
import com.alilopez.kt_demohilt.features.HireBeat.domain.entities.AuthToken

fun AuthResponseDto.toDomain(): AuthToken {
    return AuthToken(
        token = this.token
    )
}