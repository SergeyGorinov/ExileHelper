package com.poe.tradeapp.core.data.models

data class LeaguesModel(
    val result: List<League>
)

data class League(
    val id: String,
    val text: String
)