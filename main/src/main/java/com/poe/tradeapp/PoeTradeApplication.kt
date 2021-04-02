package com.poe.tradeapp

import android.app.Application
import com.poe.tradeapp.charts_feature.chartsFeatureModules
import com.poe.tradeapp.core.DI
import com.poe.tradeapp.core.coreModules
import com.poe.tradeapp.core.data.ObjectBox
import com.poe.tradeapp.currency.currencyFeatureModules
import com.poe.tradeapp.exchange.exchangeFeatureModules
import com.poe.tradeapp.notifications_feature.notificationFeatureModules
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Suppress("unused")
class PoeTradeApplication : Application() {

    @ExperimentalCoroutinesApi
    override fun onCreate() {
        super.onCreate()
        DI.init(
            this,
            mainModules +
                    coreModules +
                    currencyFeatureModules +
                    exchangeFeatureModules +
                    chartsFeatureModules +
                    notificationFeatureModules
        )
        ObjectBox.init(this)
    }
}