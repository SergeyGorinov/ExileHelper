package com.poetradeapp.models

import androidx.lifecycle.ViewModel
import com.poetradeapp.http.RequestService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {

    val channel = Channel<Any>(Channel.CONFLATED)

    private var currencyData: List<StaticEntries> = listOf()
    private var currencyResultData: ExchangeCurrencyResponse? = null
    private val wantSelectedCurrencies = mutableListOf<String>()
    private val haveSelectedCurrencies = mutableListOf<String>()

    fun setMainData(data: List<StaticEntries>) {
        currencyData = data
    }

    fun getMainData() = currencyData
    fun getCurrencyResultsData() = currencyResultData

    fun addWantCurrency(id: String) {
        wantSelectedCurrencies.add(id)
    }

    fun removeWantCurrency(id: String) {
        wantSelectedCurrencies.remove(id)
    }

    fun addHaveCurrency(id: String) {
        haveSelectedCurrencies.add(id)
    }

    fun removeHaveCurrency(id: String) {
        haveSelectedCurrencies.remove(id)
    }

    suspend fun sendCurrencyExchangeRequest() {
        val retrofit = RequestService.create("https://www.pathofexile.com/")
        val baseFetchUrl = StringBuilder("/api/trade/fetch/")

        currencyResultData = withContext(Dispatchers.Default) {
            val resultList = retrofit.getCurrencyExchangeList(
                "api/trade/exchange/Standard", ExchangeCurrencyRequestModel(
                    Exchange(want = wantSelectedCurrencies, have = haveSelectedCurrencies)
                )
            ).execute().body()

            baseFetchUrl.append(resultList?.result?.subList(0, 20)?.joinToString(separator = ","))
            baseFetchUrl.append("?query=${resultList?.id}")
            baseFetchUrl.append("&exchange")

            retrofit.getCurrencyExchangeResponse(baseFetchUrl.toString()).execute().body()
        }
    }
}