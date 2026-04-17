package com.alilopez.kt_demohilt.features.HireBeat.domain.usecases

import com.alilopez.kt_demohilt.features.HireBeat.domain.entities.CatalogItem
import com.alilopez.kt_demohilt.features.HireBeat.domain.repositories.AuthRepository
import javax.inject.Inject

class GetCatalogsUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend fun execute(): Pair<List<CatalogItem>, List<CatalogItem>> {
        val instruments = repository.getInstruments()
        val genres = repository.getGenres()
        return Pair(instruments, genres)
    }
}