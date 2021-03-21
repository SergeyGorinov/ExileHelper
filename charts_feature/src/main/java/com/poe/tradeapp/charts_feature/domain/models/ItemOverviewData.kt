package com.poe.tradeapp.charts_feature.domain.models

import android.graphics.Bitmap

internal data class ItemOverviewData(
    val id: String,
    val itemName: String,
    val itemIcon: String,
    val tradeId: String,
    val itemIconForText: Bitmap?,
    val chaosEquivalentValue: Float,
    val itemSparkLine: List<Float>?,
    val totalChange: Float
)