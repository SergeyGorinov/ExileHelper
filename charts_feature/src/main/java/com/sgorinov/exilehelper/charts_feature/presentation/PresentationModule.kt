package com.sgorinov.exilehelper.charts_feature.presentation

import com.sgorinov.exilehelper.core.presentation.FragmentScopes
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal val presentationModule = module {
    scope(named(FragmentScopes.CHARTS_FEATURE)) {
        viewModel {
            ChartsViewModel(get(), get(), get(), get(), get(), get())
        }
    }
}