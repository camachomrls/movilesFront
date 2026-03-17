package com.alilopez.kt_demohilt.core.hardware.domain

import com.alilopez.kt_demohilt.core.hardware.domain.model.LocationPoint
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    fun getLocationFlow(interval: Long): Flow<LocationPoint>
}