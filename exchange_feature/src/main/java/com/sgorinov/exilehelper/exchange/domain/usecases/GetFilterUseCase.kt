package com.sgorinov.exilehelper.exchange.domain.usecases

import com.sgorinov.exilehelper.exchange.domain.IFeatureRepository

internal class GetFilterUseCase(private val repository: IFeatureRepository) {

    fun execute(id: String, onFieldsChanged: (Boolean) -> Unit) =
        repository.getOrCreateFilter(id, onFieldsChanged)
}