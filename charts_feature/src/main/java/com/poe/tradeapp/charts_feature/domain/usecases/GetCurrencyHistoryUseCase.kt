package com.poe.tradeapp.charts_feature.domain.usecases

import com.poe.tradeapp.charts_feature.domain.IFeatureRepository
import com.poe.tradeapp.charts_feature.domain.models.CurrencyHistoryModel
import com.poe.tradeapp.charts_feature.domain.models.GraphData

internal class GetCurrencyHistoryUseCase(private val repository: IFeatureRepository) {

    suspend fun execute(league: String, type: String, id: String): CurrencyHistoryModel {
        return repository.getCurrencyHistory(league, type, id).run {
            val payGraphData = this.payCurrencyGraphData.map {
                GraphData(it.count, it.value, it.daysAgo)
            }
            val receiveGraphData = this.receiveCurrencyGraphData.map {
                GraphData(it.count, it.value, it.daysAgo)
            }
            CurrencyHistoryModel(payGraphData, receiveGraphData)
        }
    }
}