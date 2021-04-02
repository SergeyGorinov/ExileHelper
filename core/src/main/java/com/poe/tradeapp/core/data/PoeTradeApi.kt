package com.poe.tradeapp.core.data

import com.poe.tradeapp.core.data.models.RemoteNotificationRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface PoeTradeApi {

    @POST("addRequest")
    suspend fun sendRequest(@Body userRequest: RemoteNotificationRequest): Response<Void>

    @GET("getRequests")
    suspend fun getRequests(
        @Query("authorizationToken")
        authorizationToken: String,
        @Query("messagingToken")
        messagingToken: String
    ): Response<List<RemoteNotificationRequest>>

    @GET("addToken")
    suspend fun addToken(
        @Query("authorizationToken")
        authorizationToken: String,
        @Query("messagingToken")
        messagingToken: String
    ): Response<Void>
}