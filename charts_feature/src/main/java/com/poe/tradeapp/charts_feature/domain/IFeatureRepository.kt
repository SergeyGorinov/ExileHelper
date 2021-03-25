package com.poe.tradeapp.charts_feature.domain

import com.poe.tradeapp.charts_feature.data.models.GraphData
import com.poe.tradeapp.charts_feature.data.models.HistoryModel
import com.poe.tradeapp.charts_feature.data.models.ItemGroup
import com.poe.tradeapp.charts_feature.data.models.OverviewResponse
import kotlinx.serialization.json.JsonObject

internal interface IFeatureRepository {
    suspend fun getCurrenciesOverview(league: String, type: String): OverviewResponse
    suspend fun getItemsOverview(league: String, type: String): JsonObject
    suspend fun getCurrencyHistory(league: String, type: String, id: String): HistoryModel
    suspend fun getItemHistory(league: String, type: String, id: String): List<GraphData>
    fun getItemsGroups(): List<ItemGroup>
}