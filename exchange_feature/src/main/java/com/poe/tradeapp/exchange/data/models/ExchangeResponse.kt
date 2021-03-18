package com.poe.tradeapp.exchange.data.models

data class ItemsListResponseModel(
    val id: String,
    val complexity: String?,
    val result: List<String>,
    val total: Int,
    val inexact: Boolean?
)

data class ExchangeResponse(
    val result: List<ResponseModel>
)

data class ResponseModel(
    val id: String,
    val item: Item,
    val listing: Listing,
    val gone: Boolean?
)

data class Listing(
    val method: String,
    val indexed: String,
    val thread_id: String?,
    val thread_locale: String?,
    val stash: StashCell?,
    val whisper: String,
    val account: Account,
    val price: ItemPrice
)

data class StashCell(
    val name: String,
    val x: Int,
    val y: Int
)

data class Account(
    val name: String,
    val lastCharacterName: String,
    val online: Online?,
    val language: String
)

//@JsonInclude(JsonInclude.Include.NON_NULL)
data class Online(
    val league: String,
    val status: String?
)

data class ItemPrice(
    val type: String,
    val amount: Int,
    val currency: String,
    val distance: Int?
)

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
    val abyssJewel: Boolean?,
    val duplicated: Boolean?,
    val replica: Boolean?,
    val sockets: List<Socket>?,
    val name: String,
    val typeLine: String,
    val identified: String,
    val ilvl: Int,
    val itemLevel: Int?,
    val note: String?,
    val corrupted: Boolean?,
    val properties: List<Property>?,
    val requirements: List<Property>?,
    val additionalProperties: List<Property>?,
    val notableProperties: List<Property>?,
    val secDescrText: String?,
    val prophecyText: String?,
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

data class Property(
    val name: String,
//    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    val values: List<Pair<String, Int>>,
    val displayMode: Int,
    val progress: Double?,
    val type: Int?,
    val suffix: String?
)

data class Socket(
    val group: Int,
    val attr: String,
    val sColour: String
)

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
    val crafted: List<Mod>?,
    val veiled: List<Mod>?
)

data class Hashes(
//    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    val fractured: List<Pair<String, List<Int>>>?,
//    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    val synthesised: List<Pair<String, List<Int>>>?,
//    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    val enchant: List<Pair<String, List<Int>>>?,
//    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    val implicit: List<Pair<String, List<Int>>>?,
//    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    val explicit: List<Pair<String, List<Int>>>?,
//    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    val crafted: List<Pair<String, List<Int>>>?
)

data class Mod(
    val name: String?,
    val tier: String?,
    val magnitudes: List<Magnitude>?
)

data class Magnitude(
    val hash: String?,
    val min: Int?,
    val max: Int?
)

data class Hybrid(
    val isVaalGem: Boolean?,
    val baseTypeName: String?,
    val properties: List<Property>?,
    val requirements: List<Property>?,
    val implicitMods: List<String>?,
    val explicitMods: List<String>?,
    val secDescrText: String?
)

data class Influences(
    val elder: Boolean?,
    val shaper: Boolean?,
    val warlord: Boolean?,
    val hunter: Boolean?,
    val redeemer: Boolean?,
    val crusader: Boolean?
) {
    //    @JsonIgnore
    fun getInfluences() =
        listOf(
            Pair(::elder.name, elder),
            Pair(::shaper.name, shaper),
            Pair(::warlord.name, warlord),
            Pair(::hunter.name, hunter),
            Pair(::redeemer.name, redeemer),
            Pair(::crusader.name, crusader)
        ).filter { f -> f.second != null }.map { m -> m.first }
}

data class Incubated(
    val name: String?,
    val level: Int?,
    val progress: Int?,
    val total: Int?
)