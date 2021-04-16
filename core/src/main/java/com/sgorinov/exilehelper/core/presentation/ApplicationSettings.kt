package com.sgorinov.exilehelper.core.presentation

import android.content.Context

class ApplicationSettings(context: Context) {

    private val mainSettings = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    var league: String
        get() = mainSettings.getString("League", "Standard") ?: "Standard"
        set(value) = mainSettings.edit().putString("League", value).apply()
}