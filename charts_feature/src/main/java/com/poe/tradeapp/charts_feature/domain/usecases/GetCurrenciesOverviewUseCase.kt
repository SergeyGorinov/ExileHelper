package com.poe.tradeapp.charts_feature.domain.usecases

import com.poe.tradeapp.charts_feature.domain.IFeatureRepository

internal class GetCurrenciesOverviewUseCase(private val repository: IFeatureRepository) {

    suspend fun execute(league: String, type: String) {
        repository.getCurrenciesOverview(league, type)
    }
}