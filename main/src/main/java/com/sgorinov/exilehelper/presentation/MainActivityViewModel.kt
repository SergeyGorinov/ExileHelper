package com.sgorinov.exilehelper.presentation

import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.sgorinov.exilehelper.core.domain.usecases.AddTokenUseCase
import com.sgorinov.exilehelper.core.domain.usecases.GetLeaguesUseCase
import com.sgorinov.exilehelper.core.domain.usecases.UpdateStaticDataUseCase
import com.sgorinov.exilehelper.core.presentation.FirebaseUtils

internal class MainActivityViewModel(
    private val updateStaticDataUseCase: UpdateStaticDataUseCase,
    private val addTokenUseCase: AddTokenUseCase,
    private val getLeaguesUseCase: GetLeaguesUseCase
) : ViewModel() {

    var currencyExchangeMainFragmentState: Bundle? = null
    var itemsSearchMainFragmentState: Bundle? = null

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