package com.alilopez.kt_demohilt.core.hardware.domain.model

// core/location/domain/model/LocationData.kt
data class LocationPoint(
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long
) {
    init {
        require(latitude in -90.0..90.0) { "Latitud fuera de rango: $latitude" }
        require(longitude in -180.0..180.0) { "Longitud fuera de rango: $longitude" }
        require(timestamp > 0) { "Timestamp inválido" }
    }

    // Validación de frescura (ej: máximo 60 segundos de antigüedad)
    fun isFresh(maxAgeMillis: Long = 60000): Boolean {
        return (System.currentTimeMillis() - timestamp) < maxAgeMillis
    }
}