package com.sgorinov.exilehelper.currency_feature

import com.sgorinov.exilehelper.currency_feature.data.dataModule
import com.sgorinov.exilehelper.currency_feature.domain.domainModule
import com.sgorinov.exilehelper.currency_feature.presentation.presentationModule

val currencyFeatureModules = listOf(dataModule, domainModule, presentationModule)