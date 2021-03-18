package com.poe.tradeapp.currency.presentation.models

import android.graphics.Bitmap

data class CurrencyResultViewItem(
    val stock: Int,
    val pay: Int,
    val get: Int,
    val payIcon: Bitmap?,
    val getIcon: Bitmap?,
    val payLabel: String?,
    val getLabel: String?,
    val accountName: String,
    val lastCharacterName: String,
    val status: String
)
