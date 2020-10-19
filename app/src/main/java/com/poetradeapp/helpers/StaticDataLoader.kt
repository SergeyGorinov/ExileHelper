package com.poetradeapp.helpers

import com.poetradeapp.models.CurrencyGroupViewData
import com.poetradeapp.models.ItemsData
import com.poetradeapp.models.League
import com.poetradeapp.models.StatsData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StaticDataLoader @Inject constructor() {
    private val leaguesData: ArrayList<League> = arrayListOf()
    private val itemsData: ArrayList<ItemsData> = arrayListOf()
    private val statsData: ArrayList<StatsData> = arrayListOf()
    private val currencyData: ArrayList<CurrencyGroupViewData> = arrayListOf()

    fun addLeagueData(item: League) {
        leaguesData.add(item)
    }

    fun addItemsData(item: ItemsData) {
        itemsData.add(item)
    }

    fun addStatsData(item: StatsData) {
        statsData.add(item)
    }

    fun addCurrencyData(item: CurrencyGroupViewData) {
        currencyData.add(item)
    }

    fun getCurrencyData() = currencyData

    fun getItemsData() = itemsData
}