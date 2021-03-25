package com.poe.tradeapp.charts_feature.domain.models

internal data class CurrencyHistoryModel(
    val buyingGraphData: List<GraphData>,
    val sellingGraphData: List<GraphData>
)