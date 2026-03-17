package com.alilopez.kt_demohilt.core.hardware.data

import android.annotation.SuppressLint
import android.os.Looper
import com.alilopez.kt_demohilt.core.hardware.domain.LocationRepository
import com.alilopez.kt_demohilt.core.hardware.domain.model.LocationPoint
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class FusedLocationRepository @Inject constructor(
    private val fusedClient: FusedLocationProviderClient
) : LocationRepository {

    @SuppressLint("MissingPermission")
    override fun getLocationFlow(interval: Long): Flow<LocationPoint> = callbackFlow {
        val request = LocationRequest.Builder(Priority.PRIORITY_BALANCED_POWER_ACCURACY, interval)
            .setMinUpdateIntervalMillis(interval / 2)
            .build()

        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { loc ->
                    // Aplicamos validación de dominio antes de emitir
                    try {
                        val point = LocationPoint(loc.latitude, loc.longitude, loc.time)
                        if (point.isFresh()) {
                            trySend(point)
                        }
                    } catch (e: Exception) {
                        // Loggear o manejar error de validación
                    }
                }
            }
        }

        fusedClient.requestLocationUpdates(request, callback, Looper.getMainLooper())
        awaitClose { fusedClient.removeLocationUpdates(callback) }
    }
}