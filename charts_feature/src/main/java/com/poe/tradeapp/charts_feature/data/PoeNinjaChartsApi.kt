package com.poe.tradeapp.charts_feature.data

import com.poe.tradeapp.charts_feature.data.models.GraphData
import com.poe.tradeapp.charts_feature.data.models.HistoryModel
import com.poe.tradeapp.charts_feature.data.models.OverviewResponse
import kotlinx.serialization.json.JsonObject
import retrofit2.http.GET
import retrofit2.http.Query

internal interface PoeNinjaChartsApi {

    @GET("api/data/CurrencyOverview")
    suspend fun getCurrenciesOverview(
        @Query("league") league: String,
        @Query("type") type: String
    ): OverviewResponse

    @GET("api/data/ItemOverview")
    suspend fun getItemsOverview(
        @Query("league") league: String,
        @Query("type") type: String
    ): JsonObject

    @GET("api/data/CurrencyHistory")
    suspend fun getCurrencyHistory(
        @Query("league") league: String,
        @Query("type") type: String,
        @Query("currencyId") id: String
    ): HistoryModel

    @GET("api/data/ItemHistory")
    suspend fun getItemHistory(
        @Query("league") league: String,
        @Query("type") type: String,
        @Query("itemId") id: String
    ): List<GraphData>
}