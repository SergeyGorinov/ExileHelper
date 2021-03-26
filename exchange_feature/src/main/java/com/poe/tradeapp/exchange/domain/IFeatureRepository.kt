package com.poe.tradeapp.exchange.domain

import com.poe.tradeapp.exchange.data.models.ItemsRequestModel

internal interface IFeatureRepository {
    suspend fun getItemsExchangeData(league: String, data: ItemsRequestModel)
}