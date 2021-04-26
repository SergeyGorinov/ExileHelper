package com.sgorinov.exilehelper.exchange_feature.domain

import com.sgorinov.exilehelper.exchange_feature.data.models.LocalFilter
import com.sgorinov.exilehelper.exchange_feature.domain.models.ItemResultData

internal interface IFeatureRepository {

    val totalItemsResultCount: Int
    val filters: MutableList<LocalFilter>

    fun setItemData(type: String?, name: String?)
    suspend fun getItemsExchangeData(league: String, position: Int): List<ItemResultData>
}