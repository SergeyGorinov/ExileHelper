package com.poe.tradeapp.charts_feature.data

import com.poe.tradeapp.charts_feature.data.models.CurrencyHistoryModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

internal interface PoeNinjaChartsApi {

    @GET("api/data/currencyhistory")
    fun getCurrencyHistory(
        @Query("league") league: String,
        @Query("type") type: String,
        @Query("currencyid") id: String
    ): Call<CurrencyHistoryModel>
}