package com.poe.tradeapp.exchange.data

import com.poe.tradeapp.exchange.data.models.ItemsRequestModel

internal class FeatureRepository(private val apiService: ApiService) : BaseFeatureRepository() {

    override suspend fun getItemsExchangeData(league: String, data: ItemsRequestModel) {

    }
}