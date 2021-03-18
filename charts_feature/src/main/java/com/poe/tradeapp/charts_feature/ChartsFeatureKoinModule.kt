package com.poe.tradeapp.charts_feature

import com.poe.tradeapp.charts_feature.data.dataModule
import com.poe.tradeapp.charts_feature.domain.domainModule
import com.poe.tradeapp.charts_feature.presentation.presentationModule

val chartsFeatureModules = listOf(dataModule, domainModule, presentationModule)