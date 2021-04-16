package com.sgorinov.exilehelper.currency.domain.usecases

import com.sgorinov.exilehelper.currency.domain.repository.IFeatureRepository

internal class GetTotalResultCountUseCase(private val repository: IFeatureRepository) {

    fun execute() = repository.totalResultCount
}