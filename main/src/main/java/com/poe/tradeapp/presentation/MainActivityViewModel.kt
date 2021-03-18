package com.poe.tradeapp.presentation

import androidx.lifecycle.ViewModel
import com.poe.tradeapp.data.models.UserRequest
import com.poe.tradeapp.domain.usecases.SendRequestUseCase

internal class MainActivityViewModel(private val sendRequestUseCase: SendRequestUseCase) :
    ViewModel() {

    suspend fun sendRequest(userRequest: UserRequest) {
        sendRequestUseCase.execute(userRequest)
    }
}