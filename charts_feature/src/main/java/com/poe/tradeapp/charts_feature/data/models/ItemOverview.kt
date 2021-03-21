package com.poe.tradeapp.charts_feature.data.models

import kotlinx.serialization.Serializable

@Serializable
internal data class ItemsOverviewResponse(
    val lines: List<ItemOverview>,
    val language: Language
)

@Serializable
internal data class ItemOverview(
    val id: Int,
    val name: String,
    val icon: String,
    val mapTier: Int,
    val levelRequired: Int,
    val baseType: String?,
    val stackSize: Int?,
    val variant: String,
    val prophecyText: String?,
    val artFilename: String?,
    val links: Int,
    val itemClass: Int,
    val sparkline: SparkLine,
    val lowConfidenceSparkline: SparkLine,
    val implicitModifiers: List<Modifier>,
    val explicitModifiers: List<Modifier>,
    val flavourText: String,
    val corrupted: Boolean,
    val gemLevel: Int,
    val gemQuality: Int,
    val itemType: String,
    val chaosValue: Float,
    val exaltedValue: Float,
    val count: Int,
    val detailsId: String,
    val tradeInfo: String?,
    val mapRegion: String?,
    val listingCount: Int
)

@Serializable
internal data class Modifier(
    val text: String,
    val optional: Boolean
)