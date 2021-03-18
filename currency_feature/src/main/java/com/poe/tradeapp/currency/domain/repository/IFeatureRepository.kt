package com.poe.tradeapp.currency.domain.repository

import kotlinx.serialization.json.JsonObject

internal interface IFeatureRepository {

    suspend fun getCurrencyExchangeData(
        wantSelectedCurrencies: List<String>,
        haveSelectedCurrencies: List<String>
    ): JsonObject
}