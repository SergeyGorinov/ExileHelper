package com.poe.tradeapp.core

import com.poe.tradeapp.core.data.dataModule
import com.poe.tradeapp.core.domain.domainModule
import com.poe.tradeapp.core.presentation.presentationModule
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
val coreModules = listOf(dataModule, domainModule, presentationModule)