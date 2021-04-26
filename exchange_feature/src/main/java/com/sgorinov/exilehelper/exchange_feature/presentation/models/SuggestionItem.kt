package com.sgorinov.exilehelper.exchange_feature.presentation.models

internal data class SuggestionItem(
    val isHeader: Boolean,
    val text: String,
    val type: String,
    val name: String? = null
)