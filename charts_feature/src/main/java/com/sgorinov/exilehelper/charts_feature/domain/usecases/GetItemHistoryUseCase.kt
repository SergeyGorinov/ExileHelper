package com.sgorinov.exilehelper.charts_feature.domain.usecases

import com.sgorinov.exilehelper.charts_feature.domain.IFeatureRepository
import com.sgorinov.exilehelper.charts_feature.domain.models.GraphData

internal class GetItemHistoryUseCase(private val repository: IFeatureRepository) {

    suspend fun execute(league: String, type: String, id: String): List<GraphData> {
        return repository.getItemHistory(league, type, id).map {
            GraphData(it.count, it.value, it.daysAgo)
        }
    }
}