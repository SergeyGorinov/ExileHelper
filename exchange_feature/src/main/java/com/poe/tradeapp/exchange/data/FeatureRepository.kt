package com.poe.tradeapp.exchange.data

import com.poe.tradeapp.exchange.data.models.ItemsRequestModel
import kotlinx.serialization.json.JsonObject
import retrofit2.await

internal class FeatureRepository(private val apiService: ApiService) : BaseFeatureRepository() {

    override suspend fun getItemsExchangeList(
        league: String,
        data: ItemsRequestModel
    ): JsonObject {
        return apiService.getItemsExchangeList(league, data).await()
    }

    override suspend fun getItemsExchangeData(
        data: String,
        query: String
    ): JsonObject {
        return apiService.getItemExchangeResponse(data, query).await()
    }
}