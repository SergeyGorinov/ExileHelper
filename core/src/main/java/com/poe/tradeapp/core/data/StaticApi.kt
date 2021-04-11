package com.poe.tradeapp.core.data

import com.poe.tradeapp.core.data.models.ItemsModel
import com.poe.tradeapp.core.data.models.LeaguesModel
import com.poe.tradeapp.core.data.models.StaticModel
import com.poe.tradeapp.core.data.models.StatsModel
import retrofit2.http.GET

interface StaticApi {

    @GET("api/trade/data/leagues")
    suspend fun getLeagueData(): LeaguesModel

    @GET("api/trade/data/items")
    suspend fun getItemsData(): ItemsModel

    @GET("api/trade/data/stats")
    suspend fun getStatsData(): StatsModel

    @GET("api/trade/data/static")
    suspend fun getStaticData(): StaticModel
}