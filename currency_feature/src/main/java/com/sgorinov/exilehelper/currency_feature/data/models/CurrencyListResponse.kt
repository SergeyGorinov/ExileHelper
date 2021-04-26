package com.sgorinov.exilehelper.currency_feature.data.models

import kotlinx.serialization.Serializable

@Serializable
internal data class CurrencyListResponse(
    val id: String,
    val complexity: String? = null,
    val result: List<String>,
    val total: Int,
    val inexact: Boolean? = null
)