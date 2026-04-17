package com.alilopez.kt_demohilt.features.HireBeat.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alilopez.kt_demohilt.core.data.preferences.SessionManager
import com.alilopez.kt_demohilt.core.hardware.domain.VibrationManager
import com.alilopez.kt_demohilt.features.HireBeat.data.datasources.remote.models.LoginRequestDto
import com.alilopez.kt_demohilt.features.HireBeat.data.datasources.remote.models.RegisterRequestDto
import com.alilopez.kt_demohilt.features.HireBeat.domain.models.UserRole
import com.alilopez.kt_demohilt.features.HireBeat.domain.usecases.LoginUseCase
import com.alilopez.kt_demohilt.features.HireBeat.domain.usecases.RegisterUseCase
import com.alilopez.kt_demohilt.features.HireBeat.presentation.screens.AuthUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val sessionManager: SessionManager,
    private val vibrationManager: VibrationManager // <-- INYECCIÓN DE HARDWARE
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUIState())
    val uiState: StateFlow<AuthUIState> = _uiState.asStateFlow()

    fun onToggleLogin() {
        _uiState.update {
            it.copy(isLogin = !it.isLogin, error = null)
        }
    }

    fun onRoleChange(newRole: com.alilopez.kt_demohilt.features.HireBeat.domain.models.UserRole) {
        _uiState.update {
            it.copy(role = newRole)
        }
    }
    fun toggleAuthMode(isLogin: Boolean) { _uiState.update { it.copy(isLogin = isLogin) } }
    fun setRole(role: UserRole) { _uiState.update { it.copy(role = role) } }
    fun onFullNameChange(name: String) {
        _uiState.update { it.copy(fullName = name) }
    }
    fun onEmailChange(email: String) { _uiState.update { it.copy(email = email) } }
    fun onPasswordChange(password: String) { _uiState.update { it.copy(password = password) } }

    fun onSubmit() {
        val currentState = _uiState.value

        // Validación de campos vacíos con vibración
        if (currentState.email.isBlank() || currentState.password.isBlank()) {
            vibrationManager.vibrate(300) // Vibración de advertencia
            _uiState.update { it.copy(error = "Llena todos los campos para continuar") }
            return
        }

        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            try {
                if (currentState.isLogin) {
                    val authResponse =
                        loginUseCase.execute(LoginRequestDto(currentState.email, currentState.password))
                    sessionManager.saveToken(authResponse.token)
                } else {
                    val (uuidRol, nombreRol) = if (currentState.role == UserRole.MUSICIAN) {
                        // Intercambia el ID para que coincida con el rol de Músico en tu API
                        Pair("11111111-1111-1111-1111-111111111111", "Musician")
                    } else {
                        // Intercambia el ID para que coincida con el rol de Reclutador
                        Pair("22222222-2222-2222-2222-222222222222", "Recruiter")
                    }

                    registerUseCase.execute(
                        RegisterRequestDto(
                            fullname = currentState.fullName,
                            email = currentState.email,
                            password = currentState.password,
                            roleId = uuidRol,
                            roleName = nombreRol
                        )
                    )

                    val autoLoginResponse =
                        loginUseCase.execute(LoginRequestDto(currentState.email, currentState.password))

                    sessionManager.saveToken(autoLoginResponse.token)
                }

                val isNewMusician = !currentState.isLogin && currentState.role == UserRole.MUSICIAN

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isAuthenticated = true,
                        isNewMusician = isNewMusician
                    )
                }
            } catch (e: Exception) {
                // Vibración de error por fallo de credenciales o red
                vibrationManager.vibrate(500)
                _uiState.update { it.copy(isLoading = false, error = "Credenciales incorrectas o error de red") }
            }
        }
    }
}