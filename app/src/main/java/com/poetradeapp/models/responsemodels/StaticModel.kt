package com.poetradeapp.models.responsemodels

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude
data class StaticModel(
    val result: List<StaticGroup>
)

@JsonInclude
data class StaticGroup(
    val id: String,
    val label: String?,
    val entries: List<StaticItem>
)

@JsonInclude
data class StaticItem(
    val id: String,
    val text: String,
    val image: String?
)