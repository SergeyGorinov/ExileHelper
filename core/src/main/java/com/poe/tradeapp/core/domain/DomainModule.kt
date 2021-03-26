package com.poe.tradeapp.core.domain

import com.poe.tradeapp.core.domain.usecases.GetCurrencyItemsUseCase
import com.poe.tradeapp.core.domain.usecases.GetItemsUseCase
import com.poe.tradeapp.core.domain.usecases.GetLeaguesUseCase
import com.poe.tradeapp.core.domain.usecases.GetStatsUseCase
import org.koin.dsl.module

internal val domainModule = module {
    single { GetCurrencyItemsUseCase(get()) }
    single { GetLeaguesUseCase(get()) }
    single { GetItemsUseCase(get()) }
    single { GetStatsUseCase(get()) }
}