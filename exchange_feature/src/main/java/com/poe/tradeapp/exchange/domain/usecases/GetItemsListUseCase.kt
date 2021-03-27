package com.poe.tradeapp.exchange.domain.usecases

import com.poe.tradeapp.exchange.data.models.ItemsRequestModel
import com.poe.tradeapp.exchange.domain.IFeatureRepository
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive

internal class GetItemsListUseCase(private val repository: IFeatureRepository) {

    suspend fun execute(league: String, data: ItemsRequestModel): Pair<String, List<String>> {
        val response = repository.getItemsExchangeList(league, data)
        val result = response["result"]?.jsonArray?.map { it.jsonPrimitive.content } ?: listOf()
        val id = response["id"]?.jsonPrimitive?.content ?: ""
        return id to result
    }
}