package com.poetradeapp.models

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

@JsonInclude
data class ItemsListResponseModel(
    val id: String,
    val complexity: String?,
    val result: List<String>,
    val total: Int
)

@JsonInclude
data class ExchangeCurrencyResponse(
    val result: List<ExchangeCurrencyResponseModel>
)

@JsonInclude
data class ExchangeCurrencyResponseModel(
    val id: String,
    val listing: Listing,
    val item: Item
)
@JsonInclude
data class Listing(
    val method: String,
    val indexed: String,
    val stash: StashCell,
    val whisper: String,
    val account: Account,
    val price: Price
)
@JsonInclude
data class StashCell(
    val name: String,
    val x: Int,
    val y: Int
)
@JsonInclude
data class Account(
    val name: String,
    val lastCharacterName: String,
    val online: Online,
    val language: String
)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class Online(
    val league: String,
    val status: String?
)
@JsonInclude
data class Price(
    val exchange: ExchangePay,
    val item: ExchangeGet
)
@JsonInclude
data class ExchangePay(
    val currency: String,
    val amount: Int,
)
@JsonInclude
data class ExchangeGet(
    val currency: String,
    val amount: Int,
    val stock: Int,
    val id: String
)
@JsonInclude
data class Item(
    val verified: Boolean,
    val w: Int,
    val h: Int,
    val icon: String,
    val league: String,
    val name: String,
    val typeLine: String,
    val identified: String,
    val ilvl: Int,
    val note: String,
    val properties: List<Property>,
    val explicitMods: List<String>,
    val descrText: String,
    val frameType: Int,
    val stackSize: Int,
    val maxStackSize: Int
)
@JsonInclude
data class Property(
    val name: String,
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    val values: List<Pair<String, Int>>,
    val displayMode: Int,
    val type: Int
)