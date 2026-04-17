package com.alilopez.kt_demohilt.features.HireBeat.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alilopez.kt_demohilt.core.data.preferences.SessionManager
import com.alilopez.kt_demohilt.features.HireBeat.domain.usecases.GetMyProfileUseCase
import com.alilopez.kt_demohilt.features.HireBeat.presentation.screens.FeedUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val getMyProfileUseCase: GetMyProfileUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(FeedUIState())
    val uiState: StateFlow<FeedUIState> = _uiState.asStateFlow()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val userProfile = getMyProfileUseCase.execute()
                _uiState.update { it.copy(isLoading = false, profile = userProfile) }
            } catch (e: Exception) {
                // Si falla (ej. token expirado), mostramos el error
                _uiState.update { it.copy(isLoading = false, error = e.message ?: "No se pudo cargar el perfil") }
            }
        }
    }

    fun logout(onLogoutSuccess: () -> Unit) {
        viewModelScope.launch {
            sessionManager.clearToken()
            onLogoutSuccess()
        }
    }
}