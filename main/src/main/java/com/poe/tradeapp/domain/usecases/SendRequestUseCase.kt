package com.poe.tradeapp.domain.usecases

import com.poe.tradeapp.data.models.UserRequest
import com.poe.tradeapp.domain.IMainRepository

internal class SendRequestUseCase(private val repository: IMainRepository) {

    suspend fun execute(userRequest: UserRequest) {
        repository.sendRequest(userRequest)
    }
}