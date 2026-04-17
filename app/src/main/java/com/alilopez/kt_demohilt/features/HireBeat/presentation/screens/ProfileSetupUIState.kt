package com.alilopez.kt_demohilt.features.HireBeat.presentation.screens

import com.alilopez.kt_demohilt.features.HireBeat.domain.entities.CatalogItem

// Clases de apoyo para la UI
data class ProfileLinkUI(val name: String, val type: Int, val ref: String)
data class ProfileInstrumentUI(val instrumentId: Int, val level: Int, val isPrincipal: Boolean)

data class ProfileSetupUIState(
    // Campos del formulario mapeados al DTO
    val descripcion: String = "",
    val city: String = "",
    val experience: String = "", // En UI es String, se convierte a Int al enviar
    val selectedInstruments: List<ProfileInstrumentUI> = emptyList(),
    val selectedGenres: Set<Int> = emptySet(),
    val links: List<ProfileLinkUI> = emptyList(),

    // Catálogos cargados desde el Backend
    val availableInstruments: List<CatalogItem> = emptyList(),
    val availableGenres: List<CatalogItem> = emptyList(),

    // Estados de la vista
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)