package com.poe.tradeapp.exchange.data

import com.poe.tradeapp.exchange.data.models.ExchangeResponse
import com.poe.tradeapp.exchange.data.models.ItemsListResponseModel
import com.poe.tradeapp.exchange.data.models.ItemsRequestModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

@ExperimentalCoroutinesApi
internal interface ApiService {

    @POST
    fun getItemsExchangeList(
        @Url url: String,
        @Body body: ItemsRequestModel
    ): Call<ItemsListResponseModel>

    @GET
    fun getItemExchangeResponse(@Url url: String): Call<ExchangeResponse>
}