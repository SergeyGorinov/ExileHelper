package com.poe.tradeapp.currency.data

import com.poe.tradeapp.currency.data.models.CurrencyListResponse
import com.poe.tradeapp.currency.data.models.CurrencyRequest
import kotlinx.serialization.json.JsonObject
import retrofit2.Call
import retrofit2.http.*

internal interface ApiService {

    @POST("api/trade/exchange/{league}")
    fun getCurrencyExchangeList(
        @Path("league") league: String,
        @Body body: CurrencyRequest
    ): Call<CurrencyListResponse?>

    @GET("/api/trade/fetch/{data}")
    fun getCurrencyExchangeResponse(
        @Path("data") data: String,
        @Query("query") query: String,
        @QueryName queryName: String
    ): Call<JsonObject>
}