package com.alilopez.kt_demohilt.features.HireBeat.domain.usecases

import com.alilopez.kt_demohilt.features.HireBeat.data.datasources.remote.models.ProfileSetupRequestDto
import com.alilopez.kt_demohilt.features.HireBeat.domain.entities.UserProfile
import com.alilopez.kt_demohilt.features.HireBeat.domain.repositories.AuthRepository
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend fun execute(request: ProfileSetupRequestDto): UserProfile {
        return repository.updateProfile(request)
    }
}