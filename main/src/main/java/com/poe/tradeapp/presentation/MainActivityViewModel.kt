package com.poe.tradeapp.presentation

import androidx.lifecycle.ViewModel
import com.poe.tradeapp.core.domain.usecases.AddTokenUseCase
import com.poe.tradeapp.core.domain.usecases.GetLeaguesUseCase
import com.poe.tradeapp.core.domain.usecases.UpdateStaticDataUseCase
import com.poe.tradeapp.core.presentation.FirebaseUtils

internal class MainActivityViewModel(
    private val updateStaticDataUseCase: UpdateStaticDataUseCase,
    private val addTokenUseCase: AddTokenUseCase,
    private val getLeaguesUseCase: GetLeaguesUseCase
) : ViewModel() {

    suspend fun getRemoteData() {
        updateStaticDataUseCase.execute()
    }

    suspend fun addToken(): Boolean {
        return addTokenUseCase.execute(
            FirebaseUtils.getMessagingToken(),
            FirebaseUtils.getAuthToken()
        )
    }

    fun getLeagues() = getLeaguesUseCase.execute()
}