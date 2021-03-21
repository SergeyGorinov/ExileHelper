package com.poe.tradeapp.charts_feature.domain.models

import android.graphics.Bitmap

internal data class CurrencyOverviewData(
    val id: String,
    val currencyName: String,
    val currencyIcon: String?,
    val currencyId: String?,
    val currencyIconForText: Bitmap?,
    val currencyData: CurrencyData?,
    val chaosEquivalentData: CurrencyData?,
    val currencySparkLine: List<Float>?,
    val chaosEquivalentSparkLine: List<Float>?,
    val currencyTotalChange: Float,
    val chaosEquivalentTotalChange: Float
)

internal data class CurrencyData(
    val listingCount: Int,
    val value: Double
)
