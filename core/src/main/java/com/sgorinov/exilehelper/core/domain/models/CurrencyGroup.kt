package com.sgorinov.exilehelper.core.domain.models

data class CurrencyGroup(
    val id: String,
    val label: String?,
    val items: List<CurrencyItem>
)

data class CurrencyItem(
    val id: String,
    val label: String,
    val imageUrl: String?
)
