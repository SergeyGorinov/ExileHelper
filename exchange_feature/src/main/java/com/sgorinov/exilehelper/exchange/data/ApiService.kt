package com.sgorinov.exilehelper.exchange.data

import com.sgorinov.exilehelper.exchange.data.models.ItemsRequestModel
import kotlinx.serialization.json.JsonObject
import retrofit2.http.*

internal interface ApiService {

    @POST("api/trade/search/{league}")
    suspend fun getItemsExchangeList(
        @Path("league") league: String,
        @Body body: ItemsRequestModel
    ): JsonObject

    @GET("/api/trade/fetch/{data}")
    suspend fun getItemExchangeResponse(
        @Path("data") data: String,
        @Query("query") query: String
    ): JsonObject
}