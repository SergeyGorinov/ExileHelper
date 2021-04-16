package com.sgorinov.exilehelper.charts_feature.domain

import com.sgorinov.exilehelper.charts_feature.data.models.GraphData
import com.sgorinov.exilehelper.charts_feature.data.models.HistoryModel
import com.sgorinov.exilehelper.charts_feature.data.models.ItemGroup
import com.sgorinov.exilehelper.charts_feature.domain.models.OverviewData

internal interface IFeatureRepository {

    val overviewData: List<OverviewData>

    suspend fun getCurrenciesOverview(league: String, type: String)
    suspend fun getItemsOverview(league: String, type: String)
    suspend fun getCurrencyHistory(league: String, type: String, id: String): HistoryModel
    suspend fun getItemHistory(league: String, type: String, id: String): List<GraphData>
    fun getItemsGroups(): List<ItemGroup>
}