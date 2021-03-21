package com.poe.tradeapp.core.data

import com.poe.tradeapp.core.data.models.League
import com.poe.tradeapp.core.data.models.StaticGroup
import retrofit2.await

class CoreRepository(private val staticApi: StaticApi) : BaseCoreRepository() {

    override suspend fun getCurrencyItems(): List<StaticGroup> {
        return staticApi.getStaticData("api/trade/data/static").await().result
    }

    override suspend fun getLeagues(): List<League> {
        return staticApi.getLeagueData("api/trade/data/leagues").await().result
    }
}