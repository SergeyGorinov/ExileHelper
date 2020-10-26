package com.poetradeapp.models.responsemodels

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude
data class LeaguesModel(
    val result: List<League>
)

@JsonInclude
data class League(
    val id: String,
    val text: String
)