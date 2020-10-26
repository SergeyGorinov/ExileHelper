package com.poetradeapp.models.viewmodels

import android.graphics.drawable.Drawable

data class StaticGroupViewData(
    val id: String,
    val label: String?,
    val staticItems: ArrayList<StaticItemViewData> = arrayListOf()
)

data class StaticItemViewData(
    val id: String = "",
    val label: String = "",
    val image: String? = null,
    val groupLabel: String? = null,
    var drawable: Drawable? = null
)