package com.sgorinov.exilehelper.exchange_feature

import com.sgorinov.exilehelper.exchange_feature.data.dataModule
import com.sgorinov.exilehelper.exchange_feature.domain.domainModule
import com.sgorinov.exilehelper.exchange_feature.presentation.presentationModule

val exchangeFeatureModules = listOf(dataModule, domainModule, presentationModule)