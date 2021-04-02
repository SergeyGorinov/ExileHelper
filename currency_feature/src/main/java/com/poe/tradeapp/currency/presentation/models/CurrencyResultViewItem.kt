package com.poe.tradeapp.currency.presentation.models

internal data class CurrencyResultViewItem(
    val stock: Int,
    val pay: Int,
    val get: Int,
    val payIcon: String?,
    val getIcon: String?,
    val payLabel: String?,
    val getLabel: String?,
    val accountName: String,
    val lastCharacterName: String,
    val status: String
)
