package com.poe.tradeapp.charts_feature.presentation.models

import android.graphics.drawable.Drawable
import com.github.mikephil.charting.data.LineDataSet

internal data class CurrencyOverviewViewData(
    val id: String,
    val currencyName: String,
    val currencyId: String?,
    val currencyIcon: String?,
    val currencyIconForText: Drawable?,
    val currencyData: CurrencyViewData?,
    val chaosEquivalentData: CurrencyViewData?,
    val currencySparkLine: LineDataSet?,
    val chaosEquivalentSparkLine: LineDataSet?,
    val currencyTotalChange: Float,
    val chaosEquivalentTotalChange: Float
)

internal data class CurrencyViewData(
    val listingCount: Int,
    val value: Double
)