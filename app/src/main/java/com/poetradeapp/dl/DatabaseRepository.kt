package com.poetradeapp.dl

import androidx.room.*
import com.poetradeapp.models.database.*

@Database(
    entities = [
        LeagueModel::class,
        ItemGroupModel::class,
        ItemModel::class,
        StatGroupModel::class,
        StatModel::class,
        StaticGroupModel::class,
        StaticItemModel::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class DatabaseRepository : RoomDatabase() {
    abstract fun dao(): RepositoryDao
}

@Dao
abstract class RepositoryDao {
    @Transaction
    open suspend fun insert(group: ItemGroupWithItems) {
        val groupId = insert(group.group)
        group.items.forEach {
            it.groupId = groupId
            insert(it)
        }
    }

    @Transaction
    open suspend fun insert(group: StatGroupWithItems) {
        val groupId = insert(group.group)
        group.items.forEach {
            it.groupId = groupId
            insert(it)
        }
    }

    @Transaction
    open suspend fun insert(group: StaticGroupWithItems) {
        val groupId = insert(group.group)
        group.items.forEach {
            it.groupId = groupId
            insert(it)
        }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(item: LeagueModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(item: ItemGroupModel): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(item: ItemModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(item: StatGroupModel): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(item: StatModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(item: StaticGroupModel): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(item: StaticItemModel)

    @Query("SELECT * FROM LeagueModel")
    abstract suspend fun getAllLeagues(): List<LeagueModel>

    @Transaction
    @Query("SELECT * FROM ItemGroupModel")
    abstract suspend fun getAllItems(): List<ItemGroupWithItems>

    @Transaction
    @Query("SELECT * FROM StatGroupModel")
    abstract suspend fun getAllStats(): List<StatGroupWithItems>

    @Transaction
    @Query("SELECT * FROM StaticGroupModel")
    abstract suspend fun getAllStaticItems(): List<StaticGroupWithItems>
}