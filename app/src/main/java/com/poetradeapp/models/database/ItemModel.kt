package com.poetradeapp.models.database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import org.jetbrains.annotations.NotNull

@Entity
data class ItemGroupModel(
    @PrimaryKey(autoGenerate = true)
    val itemGroupId: Int? = null,
    @NotNull
    val label: String
)

@Entity
data class ItemModel(
    @PrimaryKey(autoGenerate = true)
    val itemId: Int? = null,
    var groupId: Long? = null,
    @NotNull
    val type: String,
    @NotNull
    val text: String,
    val name: String?,
    val disc: String?,
    val unique: Boolean?,
    val prophecy: Boolean?
)

data class ItemGroupWithItems(
    @Embedded
    val group: ItemGroupModel,
    @Relation(
        parentColumn = "itemGroupId",
        entityColumn = "groupId",
        entity = ItemModel::class
    )
    val items: List<ItemModel>
)