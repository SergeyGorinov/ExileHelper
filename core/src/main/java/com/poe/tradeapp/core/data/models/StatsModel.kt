package com.poe.tradeapp.core.data.models

import kotlinx.serialization.Serializable

@Serializable
data class StatsModel(
    val result: List<StatGroup>
)

@Serializable
data class StatGroup(
    val label: String,
    val entries: List<Stat>
)

@Serializable
data class Stat(
    val id: String,
    val text: String,
    val type: String,
    val option: Options? = null
)

@Serializable
data class Options(
    val options: List<Option>
)

@Serializable
data class Option(
    val id: Int,
    val text: String
)