package com.poetradeapp.models

import android.graphics.drawable.Drawable
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude
data class StaticModel(
    val result: List<StaticEntries>
)

@JsonInclude
data class StaticEntries(
    val id: String,
    val label: String?,
    val entries: List<StaticData>
)

@JsonInclude
data class StaticData(
    val id: String,
    val text: String,
    val image: String?,
    @JsonIgnore
    var drawable: Drawable?
)