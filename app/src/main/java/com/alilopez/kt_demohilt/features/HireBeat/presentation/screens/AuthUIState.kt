package com.alilopez.kt_demohilt.features.HireBeat.presentation.screens

import com.alilopez.kt_demohilt.features.HireBeat.domain.models.UserRole

data class AuthUIState(
    val isLogin: Boolean = true,
    val fullName: String = "",
    val role: UserRole = UserRole.MUSICIAN,
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isAuthenticated: Boolean = false,
    val isNewMusician: Boolean = false
)