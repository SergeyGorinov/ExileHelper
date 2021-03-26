package com.poe.tradeapp.core.data

import com.poe.tradeapp.core.data.models.League
import com.poe.tradeapp.core.data.models.SearchItems
import com.poe.tradeapp.core.data.models.StatGroup
import com.poe.tradeapp.core.data.models.StaticGroup
import retrofit2.await

class CoreRepository(private val staticApi: StaticApi) : BaseCoreRepository() {

    override suspend fun getCurrencyItems(): List<StaticGroup> {
        return staticApi.getStaticData().await().result
    }

    override suspend fun getItems(): List<SearchItems> {
        return staticApi.getItemsData().await().result
    }

    override suspend fun getStats(): List<StatGroup> {
        return staticApi.getStatsData().await().result
    }

    override suspend fun getLeagues(): List<League> {
        return staticApi.getLeagueData().await().result
    }
}