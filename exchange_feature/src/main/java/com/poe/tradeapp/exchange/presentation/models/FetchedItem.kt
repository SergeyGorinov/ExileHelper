package com.poe.tradeapp.exchange.presentation.models

import android.text.SpannableStringBuilder
import com.poe.tradeapp.exchange.data.models.Socket

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