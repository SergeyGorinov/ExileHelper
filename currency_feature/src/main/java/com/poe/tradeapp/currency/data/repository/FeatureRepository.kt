package com.poe.tradeapp.currency.data.repository

import com.poe.tradeapp.currency.data.models.CurrencyListResponse
import com.poe.tradeapp.currency.data.models.CurrencyRequest
import com.poe.tradeapp.currency.data.models.Exchange
import com.poe.tradeapp.currency.data.models.Status
import com.poe.tradeapp.currency.data.services.ApiService
import kotlinx.serialization.json.JsonObject
import retrofit2.await

internal class FeatureRepository(private val apiService: ApiService) : BaseFeatureRepository() {

    override suspend fun getCurrencyExchangeData(
        wantSelectedCurrencies: List<String>,
        haveSelectedCurrencies: List<String>
    ): JsonObject {
        val baseFetchUrl = StringBuilder("/api/trade/fetch/")
        val resultList = getCurrencyExchangeList(
            "api/trade/exchange/Ritual", CurrencyRequest(
                Exchange(
                    status = Status("online"),
                    want = wantSelectedCurrencies,
                    have = haveSelectedCurrencies,
                    minimum = null,
                    fulfillable = null,
                    account = null
                )
            )
        )
        if (resultList.result.size > 20) {
            baseFetchUrl.append(resultList.result.subList(0, 20).joinToString(separator = ","))
        } else {
            baseFetchUrl.append(resultList.result.joinToString(separator = ","))
        }
        baseFetchUrl.append("?query=${resultList.id}")
        baseFetchUrl.append("&exchange")
        return apiService.getCurrencyExchangeResponse(baseFetchUrl.toString()).await()
    }

    private suspend fun getCurrencyExchangeList(
        url: String,
        body: CurrencyRequest
    ): CurrencyListResponse {
        return apiService.getCurrencyExchangeList(url, body).await()
    }
}