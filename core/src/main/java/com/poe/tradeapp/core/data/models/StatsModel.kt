package com.poe.tradeapp.core.data.models

data class StatsModel(
    val result: List<Stats>
)

data class Stats(
    val label: String,
    val entries: List<Stat>
)

data class Stat(
    val id: String,
    val text: String,
    val type: String,
    val option: Options?
)

data class Options(
    val options: List<Option>
)

data class Option(
    val id: Int,
    val text: String
)