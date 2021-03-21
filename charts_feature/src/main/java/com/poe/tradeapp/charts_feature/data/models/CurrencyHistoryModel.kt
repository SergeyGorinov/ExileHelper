package com.poe.tradeapp.charts_feature.data.models

import kotlinx.serialization.Serializable

@Serializable
internal data class CurrencyHistoryModel(
    val payCurrencyGraphData: List<GraphData>,
    val receiveCurrencyGraphData: List<GraphData>
)

@Serializable
internal data class GraphData(
    val count: Int,
    val value: Double,
    val daysAgo: Int
)