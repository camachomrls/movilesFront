package com.alilopez.kt_demohilt.features.HireBeat.domain.usecases

import com.alilopez.kt_demohilt.features.HireBeat.data.datasources.remote.models.LoginRequestDto
import com.alilopez.kt_demohilt.features.HireBeat.domain.entities.AuthToken
import com.alilopez.kt_demohilt.features.HireBeat.domain.repositories.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend fun execute(request: LoginRequestDto): AuthToken {
        return repository.login(request)
    }
}