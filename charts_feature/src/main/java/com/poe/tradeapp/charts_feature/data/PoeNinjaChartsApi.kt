package com.poe.tradeapp.charts_feature.data

import com.poe.tradeapp.charts_feature.data.models.CurrenciesOverviewResponse
import com.poe.tradeapp.charts_feature.data.models.CurrencyHistoryModel
import com.poe.tradeapp.charts_feature.data.models.GraphData
import kotlinx.serialization.json.JsonObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

internal interface PoeNinjaChartsApi {

    @GET("api/data/CurrencyOverview")
    fun getCurrenciesOverview(
        @Query("league") league: String,
        @Query("type") type: String
    ): Call<CurrenciesOverviewResponse>

    @GET("api/data/ItemOverview")
    fun getItemsOverview(
        @Query("league") league: String,
        @Query("type") type: String
    ): Call<JsonObject>

    @GET("api/data/CurrencyHistory")
    fun getCurrencyHistory(
        @Query("league") league: String,
        @Query("type") type: String,
        @Query("currencyId") id: String
    ): Call<CurrencyHistoryModel>

    @GET("api/data/ItemHistory")
    fun getItemHistory(
        @Query("league") league: String,
        @Query("type") type: String,
        @Query("itemId") id: String
    ): Call<List<GraphData>>
}