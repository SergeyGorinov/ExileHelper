package com.poe.tradeapp.charts_feature.data.models

internal data class CurrencyHistoryModel(
    val payCurrencyGraphData: List<GraphData>,
    val receiveCurrencyGraphData: List<GraphData>
)

internal data class GraphData(
    val count: Int,
    val value: Double,
    val daysAgo: Int
)