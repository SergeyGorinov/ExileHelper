package com.poe.tradeapp.charts_feature.presentation.models

import com.github.mikephil.charting.data.LineDataSet

internal data class OverviewViewData(
    val id: String,
    val name: String,
    val tradeId: String?,
    val icon: String?,
    val sellingListingData: ListingData,
    val buyingListingData: ListingData?,
    val sellingSparkLine: LineDataSet,
    val buyingSparkLine: LineDataSet?,
    val sellingTotalChange: Float,
    val buyingTotalChange: Float?
)

internal data class ListingData(
    val listingCount: Int,
    val value: Double
)