package com.poe.tradeapp.charts_feature.presentation.models

import com.github.mikephil.charting.data.LineDataSet

internal data class HistoryModel(
    val name: String,
    val icon: String?,
    val tradeId: String?,
    val buyingGraphData: LineDataSet?,
    val sellingGraphData: LineDataSet,
    val buyingValue: Float?,
    val sellingValue: Float
)