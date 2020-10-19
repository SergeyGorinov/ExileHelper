package com.poetradeapp.models

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude
data class ItemsListResponseModel(
    val id: String,
    val complexity: String?,
    val result: List<String>,
    val total: Int,
    val inexact: Boolean?
)

@JsonInclude
data class ExchangeCurrencyResponse(
    val result: List<ExchangeCurrencyResponseModel>
)

@JsonInclude
data class ExchangeItemsResponse(
    val result: List<ExchangeItemsResponseModel>
)

@JsonInclude
data class ExchangeItemsResponseModel(
    val id: String,
    val item: Item,
    val listing: ItemListing
)

@JsonInclude
data class ExchangeCurrencyResponseModel(
    val id: String,
    val item: Item,
    val listing: CurrencyListing
)

@JsonInclude
data class ItemListing(
    val method: String,
    val indexed: String,
    val thread_id: String?,
    val thread_locale: String?,
    val stash: StashCell?,
    val whisper: String,
    val account: Account,
    val price: ItemPrice
)

@JsonInclude
data class CurrencyListing(
    val method: String,
    val indexed: String,
    val thread_id: String?,
    val thread_locale: String?,
    val stash: StashCell?,
    val whisper: String,
    val account: Account,
    val price: CurrencyPrice
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
    val online: Online?,
    val language: String
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Online(
    val league: String,
    val status: String?
)

@JsonInclude
data class ItemPrice(
    val type: String,
    val amount: Int,
    val currency: String
)

@JsonInclude
data class CurrencyPrice(
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
    val support: Boolean?,
    val league: String,
    val influences: Influences?,
    val elder: Boolean?,
    val shaper: Boolean?,
    val fractured: Boolean?,
    val synthesised: Boolean?,
    val sockets: List<Socket>?,
    val name: String,
    val typeLine: String,
    val identified: String,
    val ilvl: Int,
    val note: String?,
    val corrupted: Boolean?,
    val properties: List<Property>?,
    val requirements: List<Property>?,
    val additionalProperties: List<Property>?,
    val secDescrText: String?,
    val talismanTier: Int?,
    val implicitMods: List<String>?,
    val explicitMods: List<String>?,
    val craftedMods: List<String>?,
    val enchantMods: List<String>?,
    val fracturedMods: List<String>?,
    val synthesisedMods: List<String>?,
    val flavourText: List<String>?,
    val descrText: String?,
    val frameType: Int?,
    val incubatedItem: Incubated?,
    val hybrid: Hybrid?,
    val stackSize: Int?,
    val maxStackSize: Int?,
    val extended: Extended?
)

@JsonInclude
data class Property(
    val name: String,
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    val values: List<Pair<String, Int>>,
    val displayMode: Int,
    val progress: Double?,
    val type: Int?
)

@JsonInclude
data class Socket(
    val group: Int,
    val attr: String,
    val sColour: String
)

@JsonInclude
data class Extended(
    val ar: Int?,
    val ar_aug: Boolean?,
    val ev: Int?,
    val ev_aug: Boolean?,
    val es: Int?,
    val es_aug: Boolean?,
    val block: Int?,
    val block_aug: Boolean?,
    val dps: Int?,
    val dps_aug: Boolean?,
    val pdps: Int?,
    val pdps_aug: Boolean?,
    val edps: Int?,
    val edps_aug: Boolean?,
    val mods: Mods?,
    val hashes: Hashes?,
    val text: String?
)

data class Mods(
    val fractured: List<Mod>?,
    val synthesised: List<Mod>?,
    val enchant: List<Mod>?,
    val implicit: List<Mod>?,
    val explicit: List<Mod>?,
    val crafted: List<Mod>?
)

data class Hashes(
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    val fractured: List<Pair<String, List<Int>>>?,
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    val synthesised: List<Pair<String, List<Int>>>?,
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    val enchant: List<Pair<String, List<Int>>>?,
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    val implicit: List<Pair<String, List<Int>>>?,
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    val explicit: List<Pair<String, List<Int>>>?,
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    val crafted: List<Pair<String, List<Int>>>?
)

@JsonInclude
data class Mod(
    val name: String?,
    val tier: String?,
    val magnitudes: List<Magnitude>?
)

@JsonInclude
data class Magnitude(
    val hash: String?,
    val min: Int?,
    val max: Int?
)

@JsonInclude
data class Hybrid(
    val isVaalGem: Boolean?,
    val baseTypeName: String?,
    val properties: List<Property>?,
    val explicitMods: List<String>?,
    val secDescrText: String?
)

@JsonInclude
data class Influences(
    val elder: Boolean?,
    val shaper: Boolean?
)

@JsonInclude
data class Incubated(
    val name: String?,
    val level: Int?,
    val progress: Int?,
    val total: Int?
)