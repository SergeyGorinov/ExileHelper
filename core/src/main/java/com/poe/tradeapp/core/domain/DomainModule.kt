package com.poe.tradeapp.core.domain

import com.poe.tradeapp.core.domain.usecases.*
import org.koin.dsl.module

internal val domainModule = module {
    single { UpdateStaticDataUseCase(get()) }
    single { GetLeaguesUseCase(get()) }
    single { GetCurrencyItemsUseCase(get()) }
    single { GetItemsUseCase(get()) }
    single { GetStatsUseCase(get()) }
    single { GetNotificationRequestsUseCase(get()) }
    single { SetNotificationRequestUseCase(get()) }
    single { AddTokenUseCase(get()) }
}