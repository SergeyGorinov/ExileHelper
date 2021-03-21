package com.poe.tradeapp.presentation

import androidx.lifecycle.ViewModel
import com.poe.tradeapp.core.domain.usecases.GetLeaguesUseCase
import com.poe.tradeapp.data.models.UserRequest
import com.poe.tradeapp.domain.usecases.SendRequestUseCase

internal class MainActivityViewModel(
    private val sendRequestUseCase: SendRequestUseCase,
    private val getLeaguesUseCase: GetLeaguesUseCase
) : ViewModel() {

    var wantItemId: String? = null
    var haveItemId: String? = null

    var leagues: List<String> = listOf()

    suspend fun sendRequest(userRequest: UserRequest) {
        sendRequestUseCase.execute(userRequest)
    }

    suspend fun getLeagues() {
        if (leagues.isEmpty()) {
            leagues = getLeaguesUseCase.execute()
        }
    }
}