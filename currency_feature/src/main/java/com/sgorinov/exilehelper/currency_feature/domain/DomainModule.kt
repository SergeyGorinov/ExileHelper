package com.sgorinov.exilehelper.currency_feature.domain

import com.sgorinov.exilehelper.core.presentation.FragmentScopes
import com.sgorinov.exilehelper.currency_feature.domain.usecases.GetCurrencyExchangeResultUseCase
import com.sgorinov.exilehelper.currency_feature.domain.usecases.GetHavingCurrencyUseCase
import com.sgorinov.exilehelper.currency_feature.domain.usecases.GetRequestingCurrenciesUseCase
import com.sgorinov.exilehelper.currency_feature.domain.usecases.GetTotalResultCountUseCase
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