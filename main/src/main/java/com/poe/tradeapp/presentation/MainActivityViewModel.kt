package com.poe.tradeapp.presentation

import androidx.lifecycle.ViewModel
import com.poe.tradeapp.core.domain.usecases.AddTokenUseCase
import com.poe.tradeapp.core.domain.usecases.GetLeaguesUseCase
import com.poe.tradeapp.core.domain.usecases.UpdateStaticDataUseCase

internal class MainActivityViewModel(
    private val updateStaticDataUseCase: UpdateStaticDataUseCase,
    private val addTokenUseCase: AddTokenUseCase,
    getLeaguesUseCase: GetLeaguesUseCase
) : ViewModel() {

    val leagues: List<String> = getLeaguesUseCase.execute()

    suspend fun getRemoteData() {
        updateStaticDataUseCase.execute()
    }

    suspend fun addToken(messagingToken: String, authorizationToken: String): Boolean {
        return addTokenUseCase.execute(messagingToken, authorizationToken)
    }
}