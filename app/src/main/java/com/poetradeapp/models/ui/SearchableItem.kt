package com.poetradeapp.models.ui

data class SearchableItem(
    val isHeader: Boolean,
    val text: String,
    val type: String,
    val name: String? = null
)