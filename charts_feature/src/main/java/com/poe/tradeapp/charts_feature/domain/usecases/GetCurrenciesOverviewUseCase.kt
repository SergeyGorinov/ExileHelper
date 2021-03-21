package com.poe.tradeapp.charts_feature.domain.usecases

import com.poe.tradeapp.charts_feature.domain.IFeatureRepository
import com.poe.tradeapp.charts_feature.domain.models.CurrencyData
import com.poe.tradeapp.charts_feature.domain.models.CurrencyOverviewData
import com.poe.tradeapp.core.presentation.dp
import com.squareup.picasso.Picasso

internal class GetCurrenciesOverviewUseCase(private val repository: IFeatureRepository) {

    suspend fun execute(league: String, type: String): List<CurrencyOverviewData> {
        val result = repository.getCurrenciesOverview(league, type)
        val currenciesOverview = result.lines
        val currenciesDetails = result.currencyDetails
        return currenciesOverview.map { currencyOverview ->
            val currencyData = if (currencyOverview.pay != null) {
                CurrencyData(currencyOverview.pay.listing_count, currencyOverview.pay.value)
            } else {
                null
            }
            val chaosEquivalentData =
                CurrencyData(currencyOverview.receive.listing_count, currencyOverview.receive.value)
            val currencyDetail =
                currenciesDetails.firstOrNull { it.id == currencyOverview.receive.get_currency_id }
            val currencyIcon = Picasso.get().load(currencyDetail?.icon).resize(24.dp, 24.dp).get()
            CurrencyOverviewData(
                currencyOverview.receive.get_currency_id.toString(),
                currencyOverview.currencyTypeName,
                currencyDetail?.icon,
                currencyDetail?.tradeId ?: currencyOverview.detailsId,
                currencyIcon,
                currencyData,
                chaosEquivalentData,
                if (currencyOverview.paySparkLine.data.any { it == null }) {
                    null
                } else {
                    currencyOverview.paySparkLine.data.filterNotNull()
                },
                if (currencyOverview.receiveSparkLine.data.any { it == null }) {
                    null
                } else {
                    currencyOverview.receiveSparkLine.data.filterNotNull()
                },
                currencyOverview.paySparkLine.totalChange,
                currencyOverview.receiveSparkLine.totalChange
            )
        }
    }
}