package com.poe.tradeapp.currency.domain.usecases

import com.poe.tradeapp.currency.domain.repository.IFeatureRepository

internal class GetHavingCurrencyUseCase(private val repository: IFeatureRepository) {

    fun execute() = repository.haveCurrencies
}