package com.poe.tradeapp.currency

import com.poe.tradeapp.currency.data.dataModule
import com.poe.tradeapp.currency.domain.domainModule
import com.poe.tradeapp.currency.presentation.presentationModule

val currencyFeatureModules = listOf(dataModule, domainModule, presentationModule)