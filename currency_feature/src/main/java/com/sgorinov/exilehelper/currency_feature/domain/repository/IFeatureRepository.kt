package com.sgorinov.exilehelper.currency_feature.domain.repository

import com.sgorinov.exilehelper.currency_feature.data.models.CurrencyResultItem

internal interface IFeatureRepository {

    val wantCurrencies: MutableList<String>
    val haveCurrencies: MutableList<String>
    val totalResultCount: Int

    suspend fun getCurrencyExchangeData(
        league: String,
        isFullfilable: Boolean,
        minimum: String?,
        position: Int
    ): List<CurrencyResultItem>
}