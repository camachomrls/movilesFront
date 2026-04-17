package com.alilopez.kt_demohilt.core.permission.data

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat.checkSelfPermission
import com.alilopez.kt_demohilt.core.permission.domain.PermissionChecker
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class AndroidPermissionChecker @Inject constructor(
    @ApplicationContext private val context: Context) : PermissionChecker {
    override fun hasLocationPermission(): Boolean {
        return checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
}