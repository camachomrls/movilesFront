package com.alilopez.kt_demohilt.features.HireBeat.presentation.viewmodels

import android.content.Context
import android.location.Geocoder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alilopez.kt_demohilt.core.hardware.domain.LocationRepository
import com.alilopez.kt_demohilt.core.permission.domain.PermissionChecker
import com.alilopez.kt_demohilt.features.HireBeat.data.datasources.remote.models.ProfileSetupRequestDto
import com.alilopez.kt_demohilt.features.HireBeat.data.datasources.remote.models.PlataformasProfileRequestDto
import com.alilopez.kt_demohilt.features.HireBeat.data.datasources.remote.models.InstrumentSelectionRequestDto
import com.alilopez.kt_demohilt.features.HireBeat.domain.usecases.GetCatalogsUseCase
import com.alilopez.kt_demohilt.features.HireBeat.domain.usecases.UpdateProfileUseCase
import com.alilopez.kt_demohilt.features.HireBeat.presentation.screens.ProfileSetupUIState
import com.alilopez.kt_demohilt.features.HireBeat.presentation.screens.ProfileLinkUI
import com.alilopez.kt_demohilt.features.HireBeat.presentation.screens.ProfileInstrumentUI
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ProfileSetupViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getCatalogsUseCase: GetCatalogsUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val locationRepository: LocationRepository,
    private val permissionChecker: PermissionChecker
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileSetupUIState())
    val uiState: StateFlow<ProfileSetupUIState> = _uiState.asStateFlow()

    init {
        loadCatalogs()
    }

    private fun loadCatalogs() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val (instruments, genres) = getCatalogsUseCase.execute()
                _uiState.update {
                    it.copy(
                        availableInstruments = instruments,
                        availableGenres = genres,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    // --- MANEJO DEL GPS ---
    fun detectCity() {
        if (!permissionChecker.hasLocationPermission()) {
            _uiState.update { it.copy(error = "Faltan permisos de ubicación") }
            return
        }

        viewModelScope.launch {
            try {
                // Tomamos la primera ubicación que se obtenga
                val location = locationRepository.getLocationFlow(5000).first()
                val geocoder = Geocoder(context, Locale.getDefault())

                @Suppress("DEPRECATION") // Para mantener compatibilidad con APIs anteriores a 33
                val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)

                if (!addresses.isNullOrEmpty()) {
                    val cityDetected = addresses[0].locality ?: addresses[0].subAdminArea ?: "Ciudad detectada"
                    _uiState.update { it.copy(city = cityDetected) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "No se pudo obtener la ubicación") }
            }
        }
    }

    // --- MANEJO DEL ACELERÓMETRO ---
    fun clearForm() {
        _uiState.update { state ->
            state.copy(
                descripcion = "",
                city = "",
                experience = "",
                selectedInstruments = emptyList(),
                selectedGenres = emptySet(),
                links = emptyList()
            )
        }
    }

    fun onDescripcionChange(desc: String) { _uiState.update { it.copy(descripcion = desc) } }
    fun onCityChange(city: String) { _uiState.update { it.copy(city = city) } }
    fun onExperienceChange(exp: String) {
        if(exp.all { it.isDigit() }) {
            _uiState.update { it.copy(experience = exp) }
        }
    }

    // --- MANEJO AVANZADO DE INSTRUMENTOS ---
    fun removeInstrument(id: Int) {
        _uiState.update { state ->
            state.copy(selectedInstruments = state.selectedInstruments.filterNot { it.instrumentId == id })
        }
    }

    fun addInstrument(id: Int, level: Int, isPrincipal: Boolean) {
        _uiState.update { state ->
            val cleanedList = state.selectedInstruments.filterNot { it.instrumentId == id }
            state.copy(selectedInstruments = cleanedList + ProfileInstrumentUI(id, level, isPrincipal))
        }
    }

    // --- MANEJO DE GÉNEROS ---
    fun toggleGenre(id: Int) {
        _uiState.update { state ->
            val newSelection = if (state.selectedGenres.contains(id)) {
                state.selectedGenres - id
            } else {
                state.selectedGenres + id
            }
            state.copy(selectedGenres = newSelection)
        }
    }

    // --- MANEJO AVANZADO DE REDES/PLATAFORMAS ---
    fun removeLink(index: Int) {
        _uiState.update { state ->
            val newLinks = state.links.toMutableList().apply { removeAt(index) }
            state.copy(links = newLinks)
        }
    }

    fun addLink(name: String, type: Int, ref: String) {
        if (name.isBlank() || ref.isBlank()) return
        _uiState.update { state ->
            val newLinks = state.links.toMutableList().apply {
                add(ProfileLinkUI(name, type, ref))
            }
            state.copy(links = newLinks)
        }
    }

    fun onSubmit() {
        val currentState = _uiState.value
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            try {
                val requestDto = ProfileSetupRequestDto(
                    city = currentState.city,
                    experience = currentState.experience.toIntOrNull() ?: 0,
                    descripcion = currentState.descripcion,
                    genres = currentState.selectedGenres.toList(),
                    instruments = currentState.selectedInstruments.map {
                        InstrumentSelectionRequestDto(
                            instrumentId = it.instrumentId,
                            level = it.level,
                            isPrincipal = it.isPrincipal
                        )
                    },
                    links = currentState.links.map {
                        PlataformasProfileRequestDto(
                            name = it.name,
                            type = it.type,
                            ref = it.ref
                        )
                    }
                )

                updateProfileUseCase.execute(requestDto)
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}