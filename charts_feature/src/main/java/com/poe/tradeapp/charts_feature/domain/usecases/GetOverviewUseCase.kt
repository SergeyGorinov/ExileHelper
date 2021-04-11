package com.poe.tradeapp.charts_feature.domain.usecases

import com.poe.tradeapp.charts_feature.domain.IFeatureRepository

internal class GetOverviewUseCase(private val repository: IFeatureRepository) {

    fun execute() = repository.overviewData
}