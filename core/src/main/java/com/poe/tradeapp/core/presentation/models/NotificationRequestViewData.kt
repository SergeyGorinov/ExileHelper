package com.poe.tradeapp.core.presentation.models

data class NotificationRequestViewData(
    val buyingItemText: String,
    val buyingItemImage: String,
    val payingItemText: String,
    val payingItemImage: String,
    val payingItemAmount: Int
)