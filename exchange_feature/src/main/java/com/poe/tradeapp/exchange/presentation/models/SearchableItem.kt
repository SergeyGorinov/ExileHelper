package com.poe.tradeapp.exchange.presentation.models

data class SearchableItem(
    val isHeader: Boolean,
    val text: String,
    val type: String,
    val name: String? = null
)