package com.sgorinov.exilehelper.exchange.presentation.models

internal data class StatGroupViewData(
    val label: String,
    val entries: List<StatViewData>
)

internal data class StatViewData(
    val id: String,
    val text: String,
    val type: String,
    val option: List<Option>?
)

internal data class Option(
    val id: Int,
    val text: String
)