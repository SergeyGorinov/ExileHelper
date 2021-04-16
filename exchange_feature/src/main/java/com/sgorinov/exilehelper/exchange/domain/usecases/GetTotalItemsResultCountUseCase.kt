package com.sgorinov.exilehelper.exchange.domain.usecases

import com.sgorinov.exilehelper.exchange.domain.IFeatureRepository

internal class GetTotalItemsResultCountUseCase(private val repository: IFeatureRepository) {

    fun execute() = repository.totalItemsResultCount
}