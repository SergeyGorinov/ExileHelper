package com.sgorinov.exilehelper.exchange

import com.sgorinov.exilehelper.exchange.data.dataModule
import com.sgorinov.exilehelper.exchange.domain.domainModule
import com.sgorinov.exilehelper.exchange.presentation.presentationModule

val exchangeFeatureModules = listOf(dataModule, domainModule, presentationModule)