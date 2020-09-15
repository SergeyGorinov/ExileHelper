package com.example.poetradeapp.models

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class RequestModel(
    val query: Query = Query(),
    val sort: Sorting = Sorting()
)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class Query(
    val status: Status = Status("online"),
    val stats: List<Stat> = listOf(Stat()),
    val filters: Filters? = null
)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class Sorting(
    val price: String? = "asc",
    val quality: String? = null,
    val pdamage: String? = null,
    val crit: String? = null,
    val aps: String? = null,
    val ilvl: String? = null,
    val pdps: String? = null,
    val edamage: String? = null,
    val edps: String? = null,
    val dps: String? = null,
    val ev: String? = null,
    val ar: String? = null,
    val es: String? = null,
    val block: String? = null
)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class Status(
    val option: String
)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class Stat(
    val type: String = "and",
    val filters: List<StatFilter> = listOf()
)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class StatFilter(
    val id: String,
    val value: MinMax?,
    val disabled: Boolean
)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class Filters(
    val map_filters: MapFilter?,
    val misc_filters: MiscFilter?,
    val socket_filters: SocketFilter?,
    val armour_filters: ArmourFilter?,
    val weapon_filters: WeaponFilter?,
    val type_filters: TypeFilter?,
    val req_filters: ReqFilter?,
    val trade_filters: TradeFilter?
)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class MapFilter(
    val disabled: Boolean,
    val filters: MapFilters
)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class MiscFilter(
    val disabled: Boolean,
    val filters: MiscFilters
)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class SocketFilter(
    val disabled: Boolean,
    val filters: SocketFilters
)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class ArmourFilter(
    val disabled: Boolean,
    val filters: ArmourFilters
)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class WeaponFilter(
    val disabled: Boolean,
    val filters: WeaponFilters
)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class TypeFilter(
    val disabled: Boolean,
    val filters: TypeFilters
)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class ReqFilter(
    val disabled: Boolean,
    val filters: ReqFilters
)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class TradeFilter(
    val disabled: Boolean,
    val filters: TradeFilters
)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class MapFilters(
    val map_tier: MinMax?,
    val map_iiq: MinMax?,
    val map_shaped: DropDown?,
    val map_blighted: DropDown?,
    val map_series: DropDown?,
    val map_packsize: MinMax?,
    val map_iir: MinMax?,
    val map_elder: DropDown?,
    val map_region: DropDown?
)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class MiscFilters(
    val quality: MinMax?,
    val gem_level: MinMax?,
    val shaper_item: DropDown?,
    val crusader_item: DropDown?,
    val hunter_item: DropDown?,
    val fractured_item: DropDown?,
    val alternate_art: DropDown?,
    val corrupted: DropDown?,
    val crafted: DropDown?,
    val enchanted: DropDown?,
    val stored_experience: MinMax?,
    val durability: MinMax?,
    val ilvl: MinMax?,
    val gem_level_progress: MinMax?,
    val elder_item: DropDown?,
    val redeemer_item: DropDown?,
    val warlord_item: DropDown?,
    val synthesised_item: DropDown?,
    val identified: DropDown?,
    val mirrored: DropDown?,
    val veiled: DropDown?,
    val talisman_tier: MinMax?,
    val stack_size: MinMax?
)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class SocketFilters(
    val sockets: Sockets?,
    val links: Sockets?
)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class ArmourFilters(
    val ar: MinMax?,
    val es: MinMax?,
    val ev: MinMax?,
    val block: MinMax?
)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class WeaponFilters(
    val damage: MinMax?,
    val crit: MinMax?,
    val pdps: MinMax?,
    val aps: MinMax?,
    val dps: MinMax?,
    val edps: MinMax?
)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class TypeFilters(
    val category: DropDown?,
    val rarity: DropDown?
)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class ReqFilters(
    val lvl: MinMax?,
    val dex: MinMax?,
    val str: MinMax?,
    val int: MinMax?
)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class TradeFilters(
    val account: String?,
    val indexed: DropDown?,
    val sale_type: DropDown?,
    val price: Price?
)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class Sockets(
    val r: Int?,
    val g: Int?,
    val b: Int?,
    val w: Int?,
    val min: Int?,
    val max: Int?
)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class MinMax(
    val min: Int?,
    val max: Int?
)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class DropDown(
    val option: String?
)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class Price(
    val min: Int?,
    val max: Int?,
    val option: String?
)