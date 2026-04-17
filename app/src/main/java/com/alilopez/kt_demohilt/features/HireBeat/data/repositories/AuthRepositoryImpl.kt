package com.alilopez.kt_demohilt.features.HireBeat.data.repositories

import com.alilopez.kt_demohilt.core.data.preferences.SessionManager
import com.alilopez.kt_demohilt.features.HireBeat.data.datasources.remote.api.HireBeatApi
import com.alilopez.kt_demohilt.features.HireBeat.data.datasources.remote.mapper.toDomain
import com.alilopez.kt_demohilt.features.HireBeat.data.datasources.remote.models.LoginRequestDto
import com.alilopez.kt_demohilt.features.HireBeat.data.datasources.remote.models.ProfileSetupRequestDto
import com.alilopez.kt_demohilt.features.HireBeat.data.datasources.remote.models.RegisterRequestDto
import com.alilopez.kt_demohilt.features.HireBeat.domain.entities.AuthToken
import com.alilopez.kt_demohilt.features.HireBeat.domain.entities.UserProfile
import com.alilopez.kt_demohilt.features.HireBeat.domain.repositories.AuthRepository
import com.alilopez.kt_demohilt.features.HireBeat.domain.entities.CatalogItem
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: HireBeatApi,
    private val sessionManager: SessionManager
) : AuthRepository {

    override suspend fun login(request: LoginRequestDto): AuthToken {
        return api.login(request).toDomain()
    }

    override suspend fun register(request: RegisterRequestDto): AuthToken {
        return api.register(request).toDomain()
    }

    override suspend fun getMyProfile(): UserProfile {
        return api.getMyProfile().toDomain()
    }

    override suspend fun updateProfile(request: ProfileSetupRequestDto): UserProfile {
        return api.updateProfile(request).toDomain()
    }
    override suspend fun getInstruments(): List<CatalogItem> {
        return api.getInstruments().map { it.toDomain() }
    }

    override suspend fun getGenres(): List<CatalogItem> {
        return api.getGenres().map { it.toDomain() }
    }
}