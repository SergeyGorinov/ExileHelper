package com.sgorinov.exilehelper.currency.domain.models

internal data class CurrencyResultItem(
    val stock: Int,
    val pay: Int,
    val get: Int,
    val payLabel: String,
    val payImageUrl: String?,
    val getLabel: String,
    val getImageUrl: String?,
    val accountName: String,
    val lastCharacterName: String,
    val status: String
)
