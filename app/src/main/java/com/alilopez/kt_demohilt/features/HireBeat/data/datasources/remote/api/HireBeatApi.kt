package com.alilopez.kt_demohilt.features.HireBeat.data.datasources.remote.api

import com.alilopez.kt_demohilt.features.HireBeat.data.datasources.remote.models.AuthResponseDto
import com.alilopez.kt_demohilt.features.HireBeat.data.datasources.remote.models.LoginRequestDto
import com.alilopez.kt_demohilt.features.HireBeat.data.datasources.remote.models.ProfileResponseDto
import com.alilopez.kt_demohilt.features.HireBeat.data.datasources.remote.models.ProfileSetupRequestDto
import com.alilopez.kt_demohilt.features.HireBeat.data.datasources.remote.models.RegisterRequestDto
import com.alilopez.kt_demohilt.features.HireBeat.data.datasources.remote.models.SimpleCatalogResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface HireBeatApi {
    @POST("/auth/login")
    suspend fun login(@Body request: LoginRequestDto): AuthResponseDto

    @POST("/auth/register")
    suspend fun register(@Body request: RegisterRequestDto): AuthResponseDto

    @GET("/profile/me")
    suspend fun getMyProfile(): ProfileResponseDto

    @PUT("/profile")
    suspend fun updateProfile(
        @Body request: ProfileSetupRequestDto
    ): ProfileResponseDto

    @GET("/instruments")
    suspend fun getInstruments(): List<SimpleCatalogResponseDto>

    @GET("/genre") // Nota: "genre" está en singular según tu backend
    suspend fun getGenres(): List<SimpleCatalogResponseDto>
}