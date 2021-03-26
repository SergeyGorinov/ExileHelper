package com.poe.tradeapp.exchange.data

import com.poe.tradeapp.exchange.data.models.ItemsListResponseModel
import com.poe.tradeapp.exchange.data.models.ItemsRequestModel
import kotlinx.serialization.json.JsonObject
import retrofit2.Call
import retrofit2.http.*

internal interface ApiService {

    @POST("api/trade/search/{league}")
    fun getItemsExchangeList(
        @Path("league") league: String,
        @Body body: ItemsRequestModel
    ): Call<ItemsListResponseModel>

    @GET("/api/trade/fetch/{data}")
    fun getItemExchangeResponse(
        @Path("data") data: String,
        @Query("query") query: String
    ): Call<JsonObject>
}