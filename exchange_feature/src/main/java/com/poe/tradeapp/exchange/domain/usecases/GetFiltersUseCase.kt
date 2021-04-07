package com.poe.tradeapp.exchange.domain.usecases

import com.poe.tradeapp.exchange.domain.IFeatureRepository

internal class GetFiltersUseCase(private val repository: IFeatureRepository) {

    fun execute() = repository.filters
}