package com.poe.tradeapp

import com.poe.tradeapp.data.dataModule
import com.poe.tradeapp.domain.domainModule
import com.poe.tradeapp.presentation.presentationModule

val mainModules = listOf(dataModule, domainModule, presentationModule)