package com.poe.tradeapp.exchange.data.models

import kotlinx.serialization.Serializable

@Serializable
data class ItemsListResponseModel(
    val id: String,
    val complexity: String?,
    val result: List<String>,
    val total: Int,
    val inexact: Boolean?
)
