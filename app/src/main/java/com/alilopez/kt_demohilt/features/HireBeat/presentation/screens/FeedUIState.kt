package com.alilopez.kt_demohilt.features.HireBeat.presentation.screens

import com.alilopez.kt_demohilt.features.HireBeat.domain.entities.UserProfile

data class FeedUIState(
    val isLoading: Boolean = true,
    val profile: UserProfile? = null,
    val error: String? = null
)