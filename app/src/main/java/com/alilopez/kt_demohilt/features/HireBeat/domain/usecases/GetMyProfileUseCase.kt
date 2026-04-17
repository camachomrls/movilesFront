package com.alilopez.kt_demohilt.features.HireBeat.domain.usecases

import com.alilopez.kt_demohilt.features.HireBeat.domain.entities.UserProfile
import com.alilopez.kt_demohilt.features.HireBeat.domain.repositories.AuthRepository // O ProfileRepository si decides separarlo después
import javax.inject.Inject

class GetMyProfileUseCase @Inject constructor(
    private val repository: AuthRepository // Asumiendo que agregaste getMyProfile() a este repo para simplificar por ahora
) {
    suspend fun execute(): UserProfile {
        return repository.getMyProfile()
    }
}