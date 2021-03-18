package com.poe.tradeapp.core.domain

import com.poe.tradeapp.core.domain.usecases.GetCurrencyItemsUseCase
import org.koin.dsl.module

internal val domainModule = module {
    single { GetCurrencyItemsUseCase(get()) }
}