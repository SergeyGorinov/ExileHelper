package com.poe.tradeapp.exchange.domain

import com.poe.tradeapp.core.presentation.FragmentScopes
import com.poe.tradeapp.exchange.domain.usecases.GetFiltersUseCase
import com.poe.tradeapp.exchange.domain.usecases.GetItemsResultDataUseCase
import com.poe.tradeapp.exchange.domain.usecases.GetTotalItemsResultCountUseCase
import com.poe.tradeapp.exchange.domain.usecases.SetItemDataUseCase
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal val domainModule = module {
    scope(named(FragmentScopes.EXCHANGE_FEATURE)) {
        scoped { GetFiltersUseCase(get()) }
        scoped { GetItemsResultDataUseCase(get()) }
        scoped { GetTotalItemsResultCountUseCase(get()) }
        scoped { SetItemDataUseCase(get()) }
    }
}