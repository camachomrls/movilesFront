package com.alilopez.kt_demohilt.core.di

import com.alilopez.kt_demohilt.core.hardware.data.AndroidFlashManager
import com.alilopez.kt_demohilt.core.hardware.data.FusedLocationRepository
import com.alilopez.kt_demohilt.core.hardware.domain.FlashManager
import com.alilopez.kt_demohilt.core.hardware.domain.LocationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HardwareModule {
    @Binds
    @Singleton
    abstract fun bindFlashManager(
        impl: AndroidFlashManager
    ): FlashManager

    @Binds
    @Singleton
    abstract fun bindFusedLocationManager(
        impl: FusedLocationRepository
    ): LocationRepository
}