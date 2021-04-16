package com.sgorinov.exilehelper.currency.data.models

internal data class CurrencyResultItem(
    val stock: Int,
    val pay: Int,
    val get: Int,
    val payCurrencyId: String,
    val getCurrencyId: String,
    val accountName: String,
    val lastCharacterName: String,
    val status: String
)
