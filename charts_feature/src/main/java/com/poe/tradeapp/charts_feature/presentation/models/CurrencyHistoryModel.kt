package com.poe.tradeapp.charts_feature.presentation.models

import com.github.mikephil.charting.data.LineDataSet

internal data class CurrencyHistoryModel(
    val payCurrencyGraphData: LineDataSet,
    val receiveCurrencyGraphData: LineDataSet
)

internal data class GraphData(
    val count: Int,
    val value: Float,
    val daysAgo: Int
)