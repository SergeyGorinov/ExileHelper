package com.poe.tradeapp.presentation

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val presentationModule = module {
    viewModel { MainActivityViewModel(get(), get(), get()) }
}