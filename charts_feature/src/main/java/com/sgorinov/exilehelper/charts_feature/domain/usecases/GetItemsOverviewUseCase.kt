package com.sgorinov.exilehelper.charts_feature.domain.usecases

import com.sgorinov.exilehelper.charts_feature.domain.IFeatureRepository

internal class GetItemsOverviewUseCase(private val repository: IFeatureRepository) {

    suspend fun execute(league: String, type: String) {
        repository.getItemsOverview(league, type)
    }
}