package com.poe.tradeapp.currency.domain.repository

import com.poe.tradeapp.currency.data.models.CurrencyResultItem

internal interface IFeatureRepository {

    val wantCurrencies: MutableList<String>
    val haveCurrencies: MutableList<String>
    val totalResultCount: Int

    suspend fun getCurrencyExchangeData(league: String, position: Int): List<CurrencyResultItem>
}