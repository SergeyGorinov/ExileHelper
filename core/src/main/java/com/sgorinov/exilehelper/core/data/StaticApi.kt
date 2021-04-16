package com.sgorinov.exilehelper.core.data

import com.sgorinov.exilehelper.core.data.models.ItemsModel
import com.sgorinov.exilehelper.core.data.models.LeaguesModel
import com.sgorinov.exilehelper.core.data.models.StaticModel
import com.sgorinov.exilehelper.core.data.models.StatsModel
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