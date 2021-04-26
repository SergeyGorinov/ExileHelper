package com.sgorinov.exilehelper.exchange_feature.domain.models

internal data class ItemResultData(
    val name: String?,
    val typeLine: String,
    val iconUrl: String,
    val frameType: Int?,
    val synthesised: Boolean?,
    val replica: Boolean?,
    val corrupted: Boolean?,
    val sockets: List<Socket>?,
    val hybridTypeLine: String? = null,
    val influences: Influences,
    val itemData: ItemData,
    val hybridData: HybridData? = null
)

internal data class Socket(
    val group: Int,
    val attr: String,
    val sColour: String
)

internal data class Influences(
    val elder: Boolean?,
    val shaper: Boolean?,
    val warlord: Boolean?,
    val hunter: Boolean?,
    val redeemer: Boolean?,
    val crusader: Boolean?
)

internal data class ItemData(
    val properties: List<Property>,
    val requirements: List<Property>,
    val secDescrText: String?,
    val implicitMods: List<String>?,
    val explicitMods: List<String>?,
    val note: String?
)

internal data class HybridData(
    val isVaalGem: Boolean?,
    val properties: List<Property>,
    val requirements: List<Property>,
    val secDescrText: String?,
    val implicitMods: List<String>?,
    val explicitMods: List<String>?,
    val note: String?
)

internal data class Property(
    val name: String,
    val values: List<PropertyData>,
    val displayMode: Int,
    val progress: Double?,
    val type: Int?,
    val suffix: String?
)

internal data class PropertyData(
    val propertyValue: String,
    val propertyColor: Int
)