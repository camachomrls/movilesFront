package com.alilopez.kt_demohilt.core.permission.domain

interface PermissionChecker {
    fun hasLocationPermission(): Boolean
}