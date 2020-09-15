package com.example.poetradeapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

class DBData (
    val Id: Long,
    val GroupId: String,
    val GroupDescription: String?,
    val ItemId: String,
    val ItemDescription: String?,
    val ItemImagePath: ByteArray
)

class DataParser : RowParser<DBData> {
    override fun parseRow(columns: Array<Any?>): DBData {
        return DBData(
            columns[0] as Long,
            columns[1] as String,
            columns[2] as String,
            columns[3] as String,
            columns[4] as String,
            columns[5] as ByteArray
        )
    }
}

class DatabaseRepository private constructor(ctx: Context) :
    ManagedSQLiteOpenHelper(ctx, "Database", null, 1) {
    init {
        instance = this
    }

    companion object {
        private var instance: DatabaseRepository? = null

        @Synchronized
        fun getInstance(ctx: Context) = instance ?: DatabaseRepository(ctx.applicationContext)
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.createTable(
            "StaticData", true,
            "Id" to INTEGER + PRIMARY_KEY + UNIQUE + NOT_NULL,
            "GroupId" to TEXT + NOT_NULL,
            "GroupDescription" to TEXT,
            "ItemId" to TEXT + NOT_NULL,
            "ItemDescription" to TEXT,
            "ItemImage" to BLOB
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }
}

val Context.database: DatabaseRepository
    get() = DatabaseRepository.getInstance(this)