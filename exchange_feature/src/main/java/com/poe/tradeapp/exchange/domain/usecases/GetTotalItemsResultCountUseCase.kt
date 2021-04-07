package com.poe.tradeapp.exchange.domain.usecases

import com.poe.tradeapp.exchange.domain.IFeatureRepository

internal class GetTotalItemsResultCountUseCase(private val repository: IFeatureRepository) {

    fun execute() = repository.totalItemsResultCount
}