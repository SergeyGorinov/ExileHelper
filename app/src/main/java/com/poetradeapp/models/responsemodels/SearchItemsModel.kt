package com.poetradeapp.models.responsemodels

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude
data class SearchItemsModel(
    val result: List<SearchItems>
)

@JsonInclude
data class SearchItems(
    val label: String,
    val entries: List<SearchItem>
)

@JsonInclude
data class SearchItem(
    val type: String,
    val text: String,
    val name: String?,
    val disc: String?,
    val flags: Flag?
)

@JsonInclude
data class Flag(
    val unique: Boolean?,
    val prophecy: Boolean?
)