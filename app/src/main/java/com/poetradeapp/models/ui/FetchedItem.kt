package com.poetradeapp.models.ui

import android.text.SpannableStringBuilder
import com.poetradeapp.models.response.Socket

data class FetchedItem(
    val name: String?,
    val typeLine: String,
    val iconUrl: String,
    val sockets: List<Socket>?,
    val frameType: Int?,
    val backgroundColorId: Int,
    val influenceIcons: List<Int?>,
    val itemTextData: SpannableStringBuilder,
    val hybridTypeLine: String? = null,
    val hybridItemTextData: SpannableStringBuilder? = null
)