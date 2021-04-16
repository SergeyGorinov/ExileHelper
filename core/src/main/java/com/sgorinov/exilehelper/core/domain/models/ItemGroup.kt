package com.sgorinov.exilehelper.core.domain.models

data class ItemGroup(
    val groupName: String,
    val entries: List<ItemData>
)

data class ItemData(
    val type: String,
    val text: String,
    val name: String?,
    val disc: String?,
    val flags: ItemFlag?
)

data class ItemFlag(
    val unique: Boolean?,
    val prophecy: Boolean?
)