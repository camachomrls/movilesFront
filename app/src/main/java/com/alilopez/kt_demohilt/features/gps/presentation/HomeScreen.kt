package com.alilopez.kt_demohilt.features.gps.presentation

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is LocationUiState.Loading -> CircularProgressIndicator()
        is LocationUiState.MissingPermissions -> {
            Text("Permiso de ubicación denegado.", color = Color.Red)
        }
        is LocationUiState.Success -> {
            Text("Lat: ${state.data.latitude}, Lng: ${state.data.longitude}")
        }
        is LocationUiState.Error -> {
            Text("Error: ${state.message}", color = Color.Red)
        }
    }
}