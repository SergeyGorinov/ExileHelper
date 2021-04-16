package com.sgorinov.exilehelper.core

import com.sgorinov.exilehelper.core.data.dataModule
import com.sgorinov.exilehelper.core.domain.domainModule
import com.sgorinov.exilehelper.core.presentation.presentationModule
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
val coreModules = listOf(dataModule, domainModule, presentationModule)