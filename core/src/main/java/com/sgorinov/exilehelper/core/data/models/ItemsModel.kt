package com.sgorinov.exilehelper.core.data.models

import kotlinx.serialization.Serializable

@Serializable
data class ItemsModel(
    val result: List<ItemGroup>
)

@Serializable
data class ItemGroup(
    val label: String,
    val entries: List<Item>
)

@Serializable
data class Item(
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