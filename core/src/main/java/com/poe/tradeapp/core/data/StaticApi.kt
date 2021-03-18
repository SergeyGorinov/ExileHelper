package com.poe.tradeapp.core.data

import com.poe.tradeapp.core.data.models.LeaguesModel
import com.poe.tradeapp.core.data.models.SearchItemsModel
import com.poe.tradeapp.core.data.models.StaticModel
import com.poe.tradeapp.core.data.models.StatsModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface StaticApi {

    @GET
    fun getLeagueData(@Url url: String): Call<LeaguesModel>

    @GET
    fun getItemsData(@Url url: String): Call<SearchItemsModel>

    @GET
    fun getStatsData(@Url url: String): Call<StatsModel>

    @GET
    fun getStaticData(@Url url: String): Call<StaticModel>
}