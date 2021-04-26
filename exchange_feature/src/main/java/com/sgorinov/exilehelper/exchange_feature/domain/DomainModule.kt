package com.sgorinov.exilehelper.exchange_feature.domain

import com.sgorinov.exilehelper.core.presentation.FragmentScopes
import com.sgorinov.exilehelper.exchange_feature.domain.usecases.GetFiltersUseCase
import com.sgorinov.exilehelper.exchange_feature.domain.usecases.GetItemsResultDataUseCase
import com.sgorinov.exilehelper.exchange_feature.domain.usecases.GetTotalItemsResultCountUseCase
import com.sgorinov.exilehelper.exchange_feature.domain.usecases.SetItemDataUseCase
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