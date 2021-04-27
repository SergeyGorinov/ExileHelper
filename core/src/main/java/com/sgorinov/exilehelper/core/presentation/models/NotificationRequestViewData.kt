package com.sgorinov.exilehelper.core.presentation.models

data class NotificationRequestViewData(
    val id: Long?,
    val buyingItemText: String,
    val buyingItemImage: String,
    val payingItemText: String,
    val payingItemImage: String,
    val payingItemAmount: Int
)