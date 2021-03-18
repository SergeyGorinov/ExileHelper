package com.poe.tradeapp.data.models

internal data class UserRequest(
    val firebaseToken: String,
    val getCurrencyId: String,
    val payCurrencyId: String,
    val desirableRatio: Float
)
