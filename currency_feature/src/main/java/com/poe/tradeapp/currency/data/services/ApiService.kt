package com.poe.tradeapp.currency.data.services

import com.poe.tradeapp.currency.data.models.CurrencyListResponse
import com.poe.tradeapp.currency.data.models.CurrencyRequest
import kotlinx.serialization.json.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

internal interface ApiService {

    @POST
    fun getCurrencyExchangeList(
        @Url url: String,
        @Body body: CurrencyRequest
    ): Call<CurrencyListResponse>

    @GET
    fun getCurrencyExchangeResponse(@Url url: String): Call<JsonObject>
}