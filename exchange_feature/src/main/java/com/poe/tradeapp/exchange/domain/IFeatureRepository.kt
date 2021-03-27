package com.poe.tradeapp.exchange.domain

import com.poe.tradeapp.exchange.data.models.ItemsRequestModel
import kotlinx.serialization.json.JsonObject

internal interface IFeatureRepository {
    suspend fun getItemsExchangeList(league: String, data: ItemsRequestModel): JsonObject
    suspend fun getItemsExchangeData(data: String, query: String): JsonObject
}