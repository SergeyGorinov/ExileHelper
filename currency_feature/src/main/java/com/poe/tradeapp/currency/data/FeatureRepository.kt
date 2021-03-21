package com.poe.tradeapp.currency.data

import com.poe.tradeapp.currency.data.models.CurrencyListResponse
import com.poe.tradeapp.currency.data.models.CurrencyRequest
import com.poe.tradeapp.currency.data.models.Exchange
import com.poe.tradeapp.currency.data.models.Status
import kotlinx.serialization.json.JsonObject
import retrofit2.await

internal class FeatureRepository(private val apiService: ApiService) : BaseFeatureRepository() {

    override suspend fun getCurrencyExchangeData(
        wantSelectedCurrencies: List<String>,
        haveSelectedCurrencies: List<String>,
        league: String
    ): JsonObject? {
        val baseFetchUrl = StringBuilder("/api/trade/fetch/")
        val resultList = getCurrencyExchangeList(
            "api/trade/exchange/$league", CurrencyRequest(
                Exchange(
                    status = Status("online"),
                    want = wantSelectedCurrencies,
                    have = haveSelectedCurrencies,
                    minimum = null,
                    fulfillable = null,
                    account = null
                )
            )
        ) ?: return null
        if (resultList.result.size > 20) {
            baseFetchUrl.append(resultList.result.subList(0, 20).joinToString(separator = ","))
        } else {
            baseFetchUrl.append(resultList.result.joinToString(separator = ","))
        }
        baseFetchUrl.append("?query=${resultList.id}")
        baseFetchUrl.append("&exchange")
        return try {
            apiService.getCurrencyExchangeResponse(baseFetchUrl.toString()).await()
        } catch (e: Exception) {
            null
        }
    }

    private suspend fun getCurrencyExchangeList(
        url: String,
        body: CurrencyRequest
    ): CurrencyListResponse? {
        return try {
            apiService.getCurrencyExchangeList(url, body).await()
        } catch (e: Exception) {
            null
        }
    }
}