package com.poetradeapp.models

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude
data class LeagueModel(
    val result: List<League>
)

@JsonInclude
data class League(
    val id: String,
    val text: String
)