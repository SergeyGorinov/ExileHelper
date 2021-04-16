package com.sgorinov.exilehelper.exchange.domain.usecases

import com.sgorinov.exilehelper.exchange.domain.IFeatureRepository
import com.sgorinov.exilehelper.exchange.domain.models.ItemResultData

internal class GetItemsResultDataUseCase(private val repository: IFeatureRepository) {

    suspend fun execute(league: String, position: Int): List<ItemResultData> {
        return repository.getItemsExchangeData(league, position)
    }
}