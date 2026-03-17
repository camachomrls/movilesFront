package com.alilopez.kt_demohilt.features.gps.presentation

import com.alilopez.kt_demohilt.core.hardware.domain.model.LocationPoint

sealed class LocationUiState {
    object Loading : LocationUiState()
    object MissingPermissions : LocationUiState() // <--- Nuevo estado
    data class Success(val data: LocationPoint) : LocationUiState()
    data class Error(val message: String) : LocationUiState()
}