package com.sgorinov.exilehelper.core.domain.usecases

import com.sgorinov.exilehelper.core.domain.ICoreRepository

class AddTokenUseCase(private val repository: ICoreRepository) {

    suspend fun execute(messagingToken: String?, authorizationToken: String?): Boolean {
        if (messagingToken == null || authorizationToken == null) {
            return false
        }
        return repository.addToken(messagingToken, authorizationToken)
    }
}