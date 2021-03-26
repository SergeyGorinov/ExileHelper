package com.poe.tradeapp.exchange.presentation.models

data class StatGroupViewData(
    val label: String,
    val entries: List<StatViewData>
)

data class StatViewData(
    val id: String,
    val text: String,
    val type: String,
    val option: List<Option>?
)

data class Option(
    val id: Int,
    val text: String
)