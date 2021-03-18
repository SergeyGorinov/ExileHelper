package com.poe.tradeapp.core.data.models

data class SearchItemsModel(
    val result: List<SearchItems>
)

data class SearchItems(
    val label: String,
    val entries: List<SearchItem>
)

data class SearchItem(
    val type: String,
    val text: String,
    val name: String?,
    val disc: String?,
    val flags: Flag?
)

data class Flag(
    val unique: Boolean?,
    val prophecy: Boolean?
)