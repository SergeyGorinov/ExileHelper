package com.sgorinov.exilehelper.core.domain.usecases

import com.sgorinov.exilehelper.core.domain.ICoreRepository

class RemoveRequestUseCase(private val repository: ICoreRepository) {

    suspend fun execute(requestId: Long) = repository.removeRequestRemote(requestId)
}