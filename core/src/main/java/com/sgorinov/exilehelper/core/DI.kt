package com.sgorinov.exilehelper.core

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.module.Module

object DI : KoinComponent {

    fun init(application: Application, modules: List<Module>) {
        startKoin {
            androidContext(application)
            modules(modules)
        }
    }
}