package com.sgorinov.exilehelper.currency_feature.domain.usecases

import com.sgorinov.exilehelper.currency_feature.domain.repository.IFeatureRepository

internal class GetTotalResultCountUseCase(private val repository: IFeatureRepository) {

    fun execute() = repository.totalResultCount
}