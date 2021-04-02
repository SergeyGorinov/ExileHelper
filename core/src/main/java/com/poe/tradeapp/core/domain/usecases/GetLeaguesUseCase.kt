package com.poe.tradeapp.core.domain.usecases

import com.poe.tradeapp.core.domain.ICoreRepository

class GetLeaguesUseCase(private val repository: ICoreRepository) {

    fun execute(): List<String> {
        return repository.leagues
    }
}