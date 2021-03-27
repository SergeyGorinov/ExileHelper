package com.poe.tradeapp.data

import com.poe.tradeapp.data.models.UserRequest

internal class MainRepository(private val api: PoeTradeApi) : BaseMainRepository() {
    override suspend fun sendRequest(userRequest: UserRequest) {
        api.sendRequest(userRequest)
    }
}