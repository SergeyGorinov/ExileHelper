package com.poe.tradeapp.charts_feature.domain

import com.poe.tradeapp.charts_feature.data.models.CurrenciesOverviewResponse
import com.poe.tradeapp.charts_feature.data.models.CurrencyHistoryModel
import com.poe.tradeapp.charts_feature.data.models.GraphData
import com.poe.tradeapp.charts_feature.data.models.ItemGroup
import kotlinx.serialization.json.JsonObject

internal interface IFeatureRepository {
    suspend fun getCurrenciesOverview(league: String, type: String): CurrenciesOverviewResponse
    suspend fun getItemsOverview(league: String, type: String): JsonObject
    suspend fun getCurrencyHistory(league: String, type: String, id: String): CurrencyHistoryModel
    suspend fun getItemHistory(league: String, type: String, id: String): List<GraphData>
    fun getItemsGroups(): List<ItemGroup>
}