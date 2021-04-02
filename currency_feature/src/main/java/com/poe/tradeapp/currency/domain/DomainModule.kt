package com.poe.tradeapp.currency.domain

import com.poe.tradeapp.core.presentation.FragmentScopes
import com.poe.tradeapp.currency.domain.usecases.GetCurrencyExchangeResultUseCase
import com.poe.tradeapp.currency.domain.usecases.GetHavingCurrencyUseCase
import com.poe.tradeapp.currency.domain.usecases.GetRequestingCurrenciesUseCase
import com.poe.tradeapp.currency.domain.usecases.GetTotalResultCountUseCase
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal val domainModule = module {
    scope(named(FragmentScopes.CURRENCY_FEATURE)) {
        scoped { GetCurrencyExchangeResultUseCase(get(), get()) }
        scoped { GetHavingCurrencyUseCase(get()) }
        scoped { GetRequestingCurrenciesUseCase(get()) }
        scoped { GetTotalResultCountUseCase(get()) }
    }
}