package com.poe.tradeapp.charts_feature.domain.usecases

import com.poe.tradeapp.charts_feature.domain.IFeatureRepository
import com.poe.tradeapp.charts_feature.domain.models.CurrencyData
import com.poe.tradeapp.charts_feature.domain.models.OverviewData

internal class GetCurrenciesOverviewUseCase(private val repository: IFeatureRepository) {

    suspend fun execute(league: String, type: String): List<OverviewData> {
        val result = repository.getCurrenciesOverview(league, type)
        val currenciesOverview = result.lines
        val currenciesDetails = result.details
        return currenciesOverview.map { currencyOverview ->
            val sellingData = if (currencyOverview.pay != null) {
                CurrencyData(currencyOverview.pay.listing_count, 1 / currencyOverview.pay.value)
            } else {
                null
            }
            val buyingData =
                CurrencyData(currencyOverview.receive.listing_count, currencyOverview.receive.value)
            val currencyDetail =
                currenciesDetails.firstOrNull { it.id == currencyOverview.receive.get_currency_id }
            OverviewData(
                currencyOverview.receive.get_currency_id.toString(),
                currencyOverview.currencyTypeName,
                currencyDetail?.icon,
                currencyDetail?.tradeId ?: currencyOverview.detailsId,
                sellingData,
                buyingData,
                if (currencyOverview.receiveSparkLine.data.any { it == null }) {
                    null
                } else {
                    currencyOverview.receiveSparkLine.data.filterNotNull()
                },
                if (currencyOverview.paySparkLine.data.any { it == null }) {
                    listOf()
                } else {
                    currencyOverview.paySparkLine.data.filterNotNull()
                },
                currencyOverview.receiveSparkLine.totalChange,
                currencyOverview.paySparkLine.totalChange
            )
        }
    }
}