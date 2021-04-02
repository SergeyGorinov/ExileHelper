package com.poe.tradeapp.core.data

import com.poe.tradeapp.core.data.models.ItemsModel
import com.poe.tradeapp.core.data.models.LeaguesModel
import com.poe.tradeapp.core.data.models.StaticModel
import com.poe.tradeapp.core.data.models.StatsModel
import retrofit2.Call
import retrofit2.http.GET

interface StaticApi {

    @GET("api/trade/data/leagues")
    fun getLeagueData(): Call<LeaguesModel>

    @GET("api/trade/data/items")
    fun getItemsData(): Call<ItemsModel>

    @GET("api/trade/data/stats")
    fun getStatsData(): Call<StatsModel>

    @GET("api/trade/data/static")
    fun getStaticData(): Call<StaticModel>
}