package com.poe.tradeapp.charts_feature.presentation.models

import android.graphics.drawable.Drawable
import com.github.mikephil.charting.data.LineDataSet

internal data class ItemOverviewViewData(
    val itemId: String,
    val itemName: String,
    val itemIcon: String,
    val itemIconForText: Drawable,
    val chaosEquivalentValue: Float,
    val itemSparkLine: LineDataSet?,
    val totalChange: Float
)