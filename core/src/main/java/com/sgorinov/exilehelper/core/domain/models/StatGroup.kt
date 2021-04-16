package com.sgorinov.exilehelper.core.domain.models

data class StatGroup(
    val groupName: String,
    val entries: List<StatData>
)

data class StatData(
    val id: String,
    val text: String,
    val type: String,
    val options: List<Option>?
)

data class Option(
    val id: Int,
    val text: String
)