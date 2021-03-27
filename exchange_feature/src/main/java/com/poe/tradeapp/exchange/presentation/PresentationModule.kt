package com.poe.tradeapp.exchange.presentation

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val presentationModule = module {
    viewModel { ItemsSearchViewModel(get(), get(), get(), get(), get()) }
}