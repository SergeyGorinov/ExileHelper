package com.poe.tradeapp.data

import com.poe.tradeapp.data.models.UserRequest
import retrofit2.http.Body
import retrofit2.http.POST

internal interface PoeTradeApi {

    @POST("addRequest")
    suspend fun sendRequest(@Body userRequest: UserRequest)
}