package com.sgorinov.exilehelper.core.data

import com.sgorinov.exilehelper.core.data.models.RemoteNotificationRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ExileHelperApi {

    @POST("addRequest")
    suspend fun sendRequest(@Body userRequest: RemoteNotificationRequest): Response<Void>

    @GET("getRequests")
    suspend fun getRequests(
        @Query("authorizationToken")
        authorizationToken: String?,
        @Query("messagingToken")
        messagingToken: String,
        @Query("type")
        type: String,
        @Query("league")
        league: String
    ): Response<List<RemoteNotificationRequest>>

    @GET("addToken")
    suspend fun addToken(
        @Query("authorizationToken")
        authorizationToken: String,
        @Query("messagingToken")
        messagingToken: String
    ): Response<Void>
}