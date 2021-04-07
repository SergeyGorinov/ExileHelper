package com.poe.tradeapp.exchange.domain

import com.poe.tradeapp.exchange.data.models.Filter
import com.poe.tradeapp.exchange.domain.models.ItemResultData

internal interface IFeatureRepository {

    val filters: MutableList<Filter>
    val totalItemsResultCount: Int

    fun setItemData(type: String, name: String?)
    suspend fun getItemsExchangeData(league: String, position: Int): List<ItemResultData>
}