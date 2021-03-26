package com.poe.tradeapp.core.data.models

import kotlinx.serialization.Serializable

@Serializable
data class SearchItemsModel(
    val result: List<SearchItems>
)

@Serializable
data class SearchItems(
    val label: String,
    val entries: List<SearchItem>
)

@Serializable
data class SearchItem(
    val type: String,
    val text: String,
    val name: String? = null,
    val disc: String? = null,
    val flags: Flag? = null
)

@Serializable
data class Flag(
    val unique: Boolean? = null,
    val prophecy: Boolean? = null
)