package com.poe.tradeapp.core.domain.usecases

import com.poe.tradeapp.core.domain.ICoreRepository

class GetLeaguesUseCase(private val repository: ICoreRepository) {

    suspend fun execute(): List<String> {
        return repository.getLeagues().map { it.id }
    }
}