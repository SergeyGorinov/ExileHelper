package com.sgorinov.exilehelper.currency

import com.sgorinov.exilehelper.currency.data.dataModule
import com.sgorinov.exilehelper.currency.domain.domainModule
import com.sgorinov.exilehelper.currency.presentation.presentationModule

val currencyFeatureModules = listOf(dataModule, domainModule, presentationModule)