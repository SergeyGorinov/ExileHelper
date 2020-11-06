package com.poetradeapp.models.response

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude
data class StatsModel(
    val result: List<Stats>
)

@JsonInclude
data class Stats(
    val label: String,
    val entries: List<Stat>
)

@JsonInclude
data class Stat(
    val id: String,
    val text: String,
    val type: String,
    val option: Options?
)

@JsonInclude
data class Options(
    val options: List<Option>
)

@JsonInclude
data class Option(
    val id: Int,
    val text: String
)