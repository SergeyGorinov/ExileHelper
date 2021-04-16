package com.sgorinov.exilehelper.core.domain.usecases

import com.sgorinov.exilehelper.core.domain.ICoreRepository

class GetLeaguesUseCase(private val repository: ICoreRepository) {

    fun execute(): List<String> {
        return repository.leagues
    }
}