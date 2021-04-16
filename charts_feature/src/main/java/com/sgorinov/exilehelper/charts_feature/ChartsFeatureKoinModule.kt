package com.sgorinov.exilehelper.charts_feature

import com.sgorinov.exilehelper.charts_feature.data.dataModule
import com.sgorinov.exilehelper.charts_feature.domain.domainModule
import com.sgorinov.exilehelper.charts_feature.presentation.presentationModule

val chartsFeatureModules = listOf(dataModule, domainModule, presentationModule)