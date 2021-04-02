package com.poe.tradeapp.core.domain.usecases

import com.poe.tradeapp.core.domain.ICoreRepository

class UpdateStaticDataUseCase(private val repository: ICoreRepository) {

    suspend fun execute() {
        repository.getLeagues()
        repository.getCurrencyItems()
        repository.getItems()
        repository.getStats()
    }
}