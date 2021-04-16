package com.sgorinov.exilehelper.charts_feature.domain.usecases

import com.sgorinov.exilehelper.charts_feature.domain.IFeatureRepository
import com.sgorinov.exilehelper.charts_feature.domain.models.CurrencyHistoryModel
import com.sgorinov.exilehelper.charts_feature.domain.models.GraphData

internal class GetCurrencyHistoryUseCase(private val repository: IFeatureRepository) {

    suspend fun execute(league: String, type: String, id: String): CurrencyHistoryModel {
        return repository.getCurrencyHistory(league, type, id).run {
            val sellingGraphData = this.payCurrencyGraphData.map {
                GraphData(it.count, it.value, it.daysAgo)
            }
            val buyingGraphData = this.receiveCurrencyGraphData.map {
                GraphData(it.count, it.value, it.daysAgo)
            }
            CurrencyHistoryModel(buyingGraphData, sellingGraphData)
        }
    }
}