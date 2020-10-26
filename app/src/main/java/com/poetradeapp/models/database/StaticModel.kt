package com.poetradeapp.models.database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import org.jetbrains.annotations.NotNull

@Entity
data class StaticGroupModel(
    @PrimaryKey(autoGenerate = true)
    val staticGroupId: Int? = null,
    @NotNull
    val id: String,
    val label: String?
)

@Entity
data class StaticItemModel(
    @PrimaryKey(autoGenerate = true)
    val itemId: Int? = null,
    var groupId: Long? = null,
    @NotNull
    val id: String,
    @NotNull
    val label: String,
    val image: String? = null
)

data class StaticGroupWithItems(
    @Embedded
    val group: StaticGroupModel,
    @Relation(
        parentColumn = "staticGroupId",
        entityColumn = "groupId",
        entity = StaticItemModel::class
    )
    val items: List<StaticItemModel>
)