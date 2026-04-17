package com.alilopez.kt_demohilt.features.HireBeat.domain.repositories

import com.alilopez.kt_demohilt.features.HireBeat.data.datasources.remote.models.LoginRequestDto
import com.alilopez.kt_demohilt.features.HireBeat.data.datasources.remote.models.ProfileSetupRequestDto
import com.alilopez.kt_demohilt.features.HireBeat.data.datasources.remote.models.RegisterRequestDto
import com.alilopez.kt_demohilt.features.HireBeat.domain.entities.AuthToken
import com.alilopez.kt_demohilt.features.HireBeat.domain.entities.CatalogItem
import com.alilopez.kt_demohilt.features.HireBeat.domain.entities.UserProfile

interface AuthRepository {
    suspend fun login(request: LoginRequestDto): AuthToken
    suspend fun register(request: RegisterRequestDto): AuthToken
    suspend fun getMyProfile(): UserProfile
    suspend fun updateProfile(request: ProfileSetupRequestDto): UserProfile
    suspend fun getInstruments(): List<CatalogItem>
    suspend fun getGenres(): List<CatalogItem>
}