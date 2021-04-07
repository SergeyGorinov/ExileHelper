package com.poe.tradeapp.exchange.domain.usecases

import com.poe.tradeapp.exchange.domain.IFeatureRepository
import com.poe.tradeapp.exchange.domain.models.ItemResultData

internal class GetItemsResultDataUseCase(private val repository: IFeatureRepository) {

    suspend fun execute(league: String, position: Int): List<ItemResultData> {
        return repository.getItemsExchangeData(league, position)
    }
}