package com.sgorinov.exilehelper.core.domain

import com.sgorinov.exilehelper.core.domain.usecases.*
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
    single { RemoveRequestUseCase(get()) }
}