package com.sgorinov.exilehelper.currency.presentation.models

internal data class CurrencyGroupViewData(
    val id: String,
    val label: String?,
    val isTextItems: Boolean,
    val staticItems: List<CurrencyViewData>
)