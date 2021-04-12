package com.poe.tradeapp.currency.data

import com.poe.tradeapp.currency.data.models.CurrencyListResponse
import com.poe.tradeapp.currency.data.models.CurrencyRequest
import kotlinx.serialization.json.JsonObject
import retrofit2.http.*

internal interface ApiService {

    @POST("api/trade/exchange/{league}")
    suspend fun getCurrencyExchangeList(
        @Path("league") league: String,
        @Body body: CurrencyRequest
    ): CurrencyListResponse?

    @GET("/api/trade/fetch/{data}")
    suspend fun getCurrencyExchangeResponse(
        @Path("data") data: String,
        @Query("query") query: String,
        @QueryName queryName: String
    ): JsonObject
}