package com.poe.tradeapp.exchange.domain

import com.poe.tradeapp.exchange.domain.usecases.GetItemsDataListUseCase
import com.poe.tradeapp.exchange.domain.usecases.GetItemsListUseCase
import org.koin.dsl.module

internal val domainModule = module {
    single { GetItemsListUseCase(get()) }
    single { GetItemsDataListUseCase(get()) }
}