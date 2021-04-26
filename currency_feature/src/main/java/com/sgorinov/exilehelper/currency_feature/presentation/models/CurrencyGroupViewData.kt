package com.sgorinov.exilehelper.currency_feature.presentation.models

internal data class CurrencyGroupViewData(
    val id: String,
    val label: String?,
    val isTextItems: Boolean,
    val staticItems: List<CurrencyViewData>
)