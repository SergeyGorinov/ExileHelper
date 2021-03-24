package com.poe.tradeapp.charts_feature.domain.models

internal data class ItemOverviewData(
    val id: String,
    val itemName: String,
    val itemIcon: String,
    val tradeId: String,
    val chaosEquivalentValue: Float,
    val itemSparkLine: List<Float>?,
    val totalChange: Float
)