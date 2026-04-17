package com.alilopez.kt_demohilt.core.di

import com.alilopez.kt_demohilt.core.permission.data.AndroidPermissionChecker
import com.alilopez.kt_demohilt.core.permission.domain.PermissionChecker
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PermissionModule {

    @Binds
    @Singleton
    abstract fun bindPermissionChecker(
        impl: AndroidPermissionChecker
    ): PermissionChecker
}