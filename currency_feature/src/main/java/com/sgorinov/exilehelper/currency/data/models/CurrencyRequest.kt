package com.sgorinov.exilehelper.currency.data.models

import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

@Serializable
data class CurrencyRequest(
    val exchange: Exchange
)

@Serializable
data class Exchange(
    @Required
    val status: Status = Status(),
    val have: List<String>,
    val want: List<String>,
    val minimum: String? = null,
    val fulfillable: Int? = 0,
    val account: String? = null
)

@Serializable
data class Status(
    @Required
    val option: String = "online"
)