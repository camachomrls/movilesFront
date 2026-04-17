package com.alilopez.kt_demohilt.core.hardware.domain

interface VibrationManager {
    fun vibrate(durationMillis: Long = 100)
}