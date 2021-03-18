package com.poe.tradeapp.charts_feature.domain

import com.poe.tradeapp.charts_feature.domain.usecases.GetCurrencyHistoryUseCase
import com.poe.tradeapp.charts_feature.domain.usecases.GetItemsGroupsUseCase
import org.koin.dsl.module

internal val domainModule = module {
    single {
        GetCurrencyHistoryUseCase(get())
    }
    single {
        GetItemsGroupsUseCase(get())
    }
}