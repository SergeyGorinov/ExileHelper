package com.poetradeapp

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class PoeTradeApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(applicationContext)
        val config = RealmConfiguration.Builder()
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(config)
    }
}