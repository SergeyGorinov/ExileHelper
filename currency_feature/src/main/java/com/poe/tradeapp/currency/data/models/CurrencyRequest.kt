package com.poe.tradeapp.currency.data.models

import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

@Serializable
internal data class CurrencyRequest(
    val exchange: Exchange
)

@Serializable
internal data class Exchange(
    @Required
    val status: Status = Status(),
    val have: List<String>,
    val want: List<String>,
    val minimum: String? = null,
    val fulfillable: Int? = 0,
    val account: String? = null
)

@Serializable
internal data class Status(
    @Required
    val option: String = "online"
)