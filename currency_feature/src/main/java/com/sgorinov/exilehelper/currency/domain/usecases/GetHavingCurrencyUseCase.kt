package com.sgorinov.exilehelper.currency.domain.usecases

import com.sgorinov.exilehelper.currency.domain.repository.IFeatureRepository

internal class GetHavingCurrencyUseCase(private val repository: IFeatureRepository) {

    fun execute() = repository.haveCurrencies
}