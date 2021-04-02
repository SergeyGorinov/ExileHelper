package com.poe.tradeapp.core.domain.models

import kotlinx.serialization.Serializable

data class NotificationRequest(
    var buyingItem: NotificationItemData,
    var payingItem: NotificationItemData,
    var payingAmount: Int
)

@Serializable
data class NotificationItemData(
    val itemName: String,
    val itemIcon: String
)
