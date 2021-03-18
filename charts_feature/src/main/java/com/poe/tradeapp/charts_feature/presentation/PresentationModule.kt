package com.poe.tradeapp.charts_feature.presentation

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val presentationModule = module {
    viewModel {
        ChartsViewModel(get(), get())
    }
}