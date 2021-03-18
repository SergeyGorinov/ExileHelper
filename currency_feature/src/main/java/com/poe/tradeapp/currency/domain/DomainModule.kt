package com.poe.tradeapp.currency.domain

import com.poe.tradeapp.currency.domain.usecases.GetCurrencyExchangeResultUseCase
import org.koin.dsl.module

internal val domainModule = module {
    single { GetCurrencyExchangeResultUseCase(get()) }
}