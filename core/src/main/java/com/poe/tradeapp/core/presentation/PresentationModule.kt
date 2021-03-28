package com.poe.tradeapp.core.presentation

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

internal val presentationModule = module {
    single { Cicerone.create() }
    single { get<Cicerone<Router>>().router }
    single { get<Cicerone<Router>>().getNavigatorHolder() }
    single { ApplicationSettings(androidContext()) }
}