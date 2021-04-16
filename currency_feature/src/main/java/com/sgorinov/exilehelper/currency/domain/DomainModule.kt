package com.sgorinov.exilehelper.currency.domain

import com.sgorinov.exilehelper.core.presentation.FragmentScopes
import com.sgorinov.exilehelper.currency.domain.usecases.GetCurrencyExchangeResultUseCase
import com.sgorinov.exilehelper.currency.domain.usecases.GetHavingCurrencyUseCase
import com.sgorinov.exilehelper.currency.domain.usecases.GetRequestingCurrenciesUseCase
import com.sgorinov.exilehelper.currency.domain.usecases.GetTotalResultCountUseCase
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