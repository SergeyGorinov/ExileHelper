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
        val resultList = getCurrencyExchangeList(
            league, CurrencyRequest(
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

        return try {
            apiService.getCurrencyExchangeResponse(
                if (resultList.result.size > 20) {
                    resultList.result.subList(0, 20).joinToString(separator = ",")
                } else {
                    resultList.result.joinToString(separator = ",")
                },
                resultList.id,
                "exchange"
            ).await()
        } catch (e: Exception) {
            null
        }
    }

    private suspend fun getCurrencyExchangeList(
        league: String,
        body: CurrencyRequest
    ): CurrencyListResponse? {
        return try {
            val test = apiService.getCurrencyExchangeList(league, body).await()
            return test
        } catch (e: Exception) {
            null
        }
    }
}