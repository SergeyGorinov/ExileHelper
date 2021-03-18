package com.poe.tradeapp.charts_feature.domain

import com.poe.tradeapp.charts_feature.data.models.CurrencyHistoryModel
import com.poe.tradeapp.charts_feature.data.models.ItemGroup

internal interface IFeatureRepository {
    suspend fun getCurrencyHistory(league: String, type: String, id: String): CurrencyHistoryModel
    fun getItemsGroups(): List<ItemGroup>
}