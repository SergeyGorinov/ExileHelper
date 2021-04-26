package com.sgorinov.exilehelper.exchange_feature.domain.usecases

import com.sgorinov.exilehelper.exchange_feature.domain.IFeatureRepository

internal class GetTotalItemsResultCountUseCase(private val repository: IFeatureRepository) {

    fun execute() = repository.totalItemsResultCount
}