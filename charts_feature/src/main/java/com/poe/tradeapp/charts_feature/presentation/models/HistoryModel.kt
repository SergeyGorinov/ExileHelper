package com.poe.tradeapp.charts_feature.presentation.models

import android.graphics.Bitmap
import com.github.mikephil.charting.data.LineDataSet

internal data class HistoryModel(
    val itemName: String,
    val itemIcon: String?,
    val itemTradeId: String?,
    val payCurrencyGraphData: LineDataSet?,
    val receiveCurrencyGraphData: LineDataSet,
    val payValue: Float?,
    val receiveValue: Float,
    val iconForText: Bitmap?,
    val tradeId: String?
)