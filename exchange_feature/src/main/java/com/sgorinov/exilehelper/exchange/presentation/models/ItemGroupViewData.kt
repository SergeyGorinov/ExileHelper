package com.sgorinov.exilehelper.exchange.presentation.models

internal data class ItemGroupViewData(
    val label: String,
    val entries: List<ItemViewData>
)

internal data class ItemViewData(
    val type: String,
    val text: String,
    val name: String?,
    val disc: String?,
    val flags: Flag?
)

internal data class Flag(
    val unique: Boolean?,
    val prophecy: Boolean?
)