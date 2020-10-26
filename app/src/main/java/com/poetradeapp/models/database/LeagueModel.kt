package com.poetradeapp.models.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity
data class LeagueModel(
    @PrimaryKey(autoGenerate = true)
    val leagueId: Int? = null,
    @NotNull
    val id: String,
    @NotNull
    val label: String
)