package com.sgorinov.exilehelper.charts_feature.domain.models

internal data class OverviewData(
    val id: String,
    val name: String,
    val type: String?,
    val icon: String?,
    val tradeId: String?,
    val sellingListingData: CurrencyData?,
    val buyingListingData: CurrencyData,
    val sellingSparkLine: List<Float>?,
    val buyingSparkLine: List<Float>,
    val sellingTotalChange: Float?,
    val buyingTotalChange: Float
)
