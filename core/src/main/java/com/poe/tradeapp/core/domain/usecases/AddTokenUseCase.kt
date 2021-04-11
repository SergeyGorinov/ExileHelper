package com.poe.tradeapp.core.domain.usecases

import com.poe.tradeapp.core.domain.ICoreRepository

class AddTokenUseCase(private val repository: ICoreRepository) {

    suspend fun execute(messagingToken: String?, authorizationToken: String?): Boolean {
        if (messagingToken == null || authorizationToken == null) {
            return false
        }
        return repository.addToken(messagingToken, authorizationToken)
    }
}