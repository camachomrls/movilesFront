package com.alilopez.kt_demohilt.features.HireBeat.data.di

import com.alilopez.kt_demohilt.core.data.preferences.SessionManager
import com.alilopez.kt_demohilt.features.HireBeat.data.datasources.remote.api.HireBeatApi
import com.alilopez.kt_demohilt.features.HireBeat.data.repositories.AuthRepositoryImpl
import com.alilopez.kt_demohilt.features.HireBeat.domain.repositories.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HireBeatNetworkModule {

    @Provides
    @Singleton
    fun provideHireBeatApi(retrofit: Retrofit): HireBeatApi {
        return retrofit.create(HireBeatApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        api: HireBeatApi,
        sessionManager: SessionManager
    ): AuthRepository {
        return AuthRepositoryImpl(api, sessionManager)
    }
}