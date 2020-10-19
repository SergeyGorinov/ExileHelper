package com.poetradeapp.models

import android.graphics.drawable.Drawable
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude
data class GetLeaguesModel(
    val result: List<League>
)

@JsonInclude
data class GetCurrenciesModel(
    val result: List<CurrencyGroupData>
)

@JsonInclude
data class GetItemsModel(
    val result: List<ItemsData>
)

@JsonInclude
data class GetStatsModel(
    val result: List<StatsData>
)

@JsonInclude
data class League(
    val id: String,
    val text: String
)

@JsonInclude
data class CurrencyGroupData(
    val id: String,
    val label: String?,
    val entries: List<CurrencyData>
)

data class CurrencyGroupViewData(
    val id: String,
    val label: String?,
    val currencies: ArrayList<CurrencyViewData> = arrayListOf()
)

data class CurrencyViewData(
    val id: String = "",
    val label: String = "",
    val image: String? = null,
    val groupLabel: String? = null,
    var drawable: Drawable? = null,
)

@JsonInclude
data class CurrencyData(
    val id: String,
    val text: String,
    val image: String?
)

@JsonInclude
data class StatsData(
    val label: String,
    val entries: List<StatData>
)

@JsonInclude
data class ItemsData(
    val label: String,
    val entries: List<ItemData>
)

@JsonInclude
data class ItemData(
    val type: String,
    val text: String,
    val name: String?,
    val disc: String?,
    val flags: Flag?
)

data class ItemViewData(
    val text: String,
    val group: String
)

@JsonInclude
data class StatData(
    val id: String,
    val text: String,
    val type: String,
    val option: Options?
)

@JsonInclude
data class Options(
    val options: List<Option>
)

@JsonInclude
data class Option(
    val id: Int,
    val text: String
)

@JsonInclude
data class Flag(
    val unique: Boolean?,
    val prophecy: Boolean?
)

