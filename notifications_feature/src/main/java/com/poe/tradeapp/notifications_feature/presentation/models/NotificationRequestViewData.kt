package com.poe.tradeapp.notifications_feature.presentation.models

internal data class NotificationRequestViewData(
    val buyingItemText: String,
    val buyingItemImage: String,
    val payingItemText: String,
    val payingItemImage: String,
    val payingItemAmount: Int
)