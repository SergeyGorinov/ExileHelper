package com.poe.tradeapp.exchange.presentation.models

internal data class SearchableItem(
    val isHeader: Boolean,
    val text: String,
    val type: String,
    val name: String? = null
)