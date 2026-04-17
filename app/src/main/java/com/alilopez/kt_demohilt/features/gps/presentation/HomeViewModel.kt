package com.alilopez.kt_demohilt.features.gps.presentation


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alilopez.kt_demohilt.core.hardware.domain.LocationRepository
import com.alilopez.kt_demohilt.core.permission.domain.PermissionChecker
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow

import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val gps: LocationRepository,
    private val permissionChecker: PermissionChecker
): ViewModel() {

    private val _uiState = MutableStateFlow<LocationUiState>(LocationUiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun startTracking() {
        if (!permissionChecker.hasLocationPermission()) {
            _uiState.value = LocationUiState.MissingPermissions
            return
        }

        viewModelScope.launch {
            gps.getLocationFlow(10000)
                .map { LocationUiState.Success(it) as LocationUiState }
                .catch { emit(LocationUiState.Error(it.message ?: "Error")) }
                .collect { _uiState.value = it }
        }
    }
}