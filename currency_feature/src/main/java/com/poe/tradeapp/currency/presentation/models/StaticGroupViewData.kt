package com.poe.tradeapp.currency.presentation.models

internal data class StaticGroupViewData(
    val id: String,
    val label: String?,
    val isTextItems: Boolean,
    val staticItems: List<StaticItemViewData>
)