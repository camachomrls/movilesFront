package com.alilopez.kt_demohilt.core.permission.data

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import com.alilopez.kt_demohilt.core.permission.domain.PermissionChecker


class AndroidPermissionChecker(private val context: Context) : PermissionChecker {
    override fun hasLocationPermission(): Boolean {
        return checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
}