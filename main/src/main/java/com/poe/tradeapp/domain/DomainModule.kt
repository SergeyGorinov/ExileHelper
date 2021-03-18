package com.poe.tradeapp.domain

import com.poe.tradeapp.domain.usecases.SendRequestUseCase
import org.koin.dsl.module

internal val domainModule = module {
    single { SendRequestUseCase(get()) }
}