package com.poe.tradeapp.core.domain.usecases

import com.poe.tradeapp.core.domain.ICoreRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class UpdateStaticDataUseCase(private val repository: ICoreRepository) {

    suspend fun execute() = coroutineScope {
        awaitAll(
            async { repository.getLeagues() },
            async { repository.getCurrencyItems() },
            async { repository.getItems() },
            async { repository.getStats() }
        )
    }
}