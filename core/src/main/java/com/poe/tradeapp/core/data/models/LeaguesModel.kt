package com.poe.tradeapp.core.data.models

import kotlinx.serialization.Serializable

@Serializable
data class LeaguesModel(
    val result: List<League>
)

@Serializable
data class League(
    val id: String,
    val text: String
)