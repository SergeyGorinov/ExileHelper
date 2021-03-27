package com.poe.tradeapp.exchange

import com.poe.tradeapp.exchange.data.dataModule
import com.poe.tradeapp.exchange.domain.domainModule
import com.poe.tradeapp.exchange.presentation.presentationModule

val exchangeFeatureModules = listOf(dataModule, domainModule, presentationModule)