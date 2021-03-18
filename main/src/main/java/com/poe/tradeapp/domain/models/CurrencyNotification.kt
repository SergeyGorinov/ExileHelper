package com.poe.tradeapp.domain.models

data class CurrencyNotification(
    val getCurrencyId: String,
    val payCurrencyId: String,
    val desirableRatio: Float
)