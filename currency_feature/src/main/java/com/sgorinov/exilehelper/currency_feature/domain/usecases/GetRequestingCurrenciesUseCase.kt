package com.sgorinov.exilehelper.currency_feature.domain.usecases

import com.sgorinov.exilehelper.currency_feature.domain.repository.IFeatureRepository

internal class GetRequestingCurrenciesUseCase(private val repository: IFeatureRepository) {

    fun execute() = repository.wantCurrencies
}