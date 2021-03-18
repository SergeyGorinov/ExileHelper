package com.poe.tradeapp.domain

import com.poe.tradeapp.data.models.UserRequest

internal interface IMainRepository {
    suspend fun sendRequest(userRequest: UserRequest)
}