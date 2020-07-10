package com.example.poetradeapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

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
            "GroupId" to TEXT,
            "GroupDescription" to TEXT,
            "ItemId" to TEXT,
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