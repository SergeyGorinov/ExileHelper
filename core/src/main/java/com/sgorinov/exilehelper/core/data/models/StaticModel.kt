package com.sgorinov.exilehelper.core.data.models

import kotlinx.serialization.Serializable

@Serializable
data class StaticModel(
    val result: List<StaticGroup>
)

@Serializable
data class StaticGroup(
    val id: String,
    val label: String? = null,
    val entries: List<StaticItem>
)

@Serializable
data class StaticItem(
    val id: String,
    val text: String,
    val image: String? = null
)