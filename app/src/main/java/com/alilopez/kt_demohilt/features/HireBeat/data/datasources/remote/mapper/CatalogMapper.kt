package com.alilopez.kt_demohilt.features.HireBeat.data.datasources.remote.mapper

import com.alilopez.kt_demohilt.features.HireBeat.data.datasources.remote.models.SimpleCatalogResponseDto
import com.alilopez.kt_demohilt.features.HireBeat.domain.entities.CatalogItem

fun SimpleCatalogResponseDto.toDomain(): CatalogItem {
    return CatalogItem(
        id = this.id,
        name = this.name
    )
}