package com.sgorinov.exilehelper.exchange.domain

import com.sgorinov.exilehelper.exchange.data.models.Filter
import com.sgorinov.exilehelper.exchange.domain.models.ItemResultData

internal interface IFeatureRepository {

    val filters: MutableList<Filter>
    val totalItemsResultCount: Int

    fun setItemData(type: String?, name: String?)
    suspend fun getItemsExchangeData(league: String, position: Int): List<ItemResultData>
}