package com.sgorinov.exilehelper.exchange.domain.usecases

import com.sgorinov.exilehelper.exchange.domain.IFeatureRepository

internal class GetFiltersUseCase(private val repository: IFeatureRepository) {

    fun execute() = repository.filters
}