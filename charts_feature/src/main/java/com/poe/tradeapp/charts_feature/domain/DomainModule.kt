package com.poe.tradeapp.charts_feature.domain

import com.poe.tradeapp.charts_feature.domain.usecases.*
import org.koin.dsl.module

internal val domainModule = module {
    single {
        GetCurrencyHistoryUseCase(get())
    }
    single {
        GetItemsGroupsUseCase(get())
    }
    single {
        GetCurrenciesOverviewUseCase(get())
    }
    single {
        GetItemsOverviewUseCase(get())
    }
    single {
        GetItemHistoryUseCase(get())
    }
}