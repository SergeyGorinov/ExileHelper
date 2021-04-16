package com.sgorinov.exilehelper.exchange.presentation.models

internal data class SuggestionItem(
    val isHeader: Boolean,
    val text: String,
    val type: String,
    val name: String? = null
)