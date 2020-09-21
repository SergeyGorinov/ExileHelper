package com.poetradeapp.models

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.poetradeapp.http.RequestService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private var currencyData: List<StaticEntries>? = null
    private var selectedCurrencies = mutableListOf<String>()

    val channel = Channel<Any>(Channel.CONFLATED)

    fun setMainData(data: List<StaticEntries>) {
        currencyData = data
    }

    fun getMainData() = currencyData

    fun addCurrency(id: String) {
        selectedCurrencies.add(id)
    }

    fun removeCurrency(id: String) {
        selectedCurrencies.remove(id)
    }

    fun sendCurrencyExchangeRequest(retrofit: RequestService) {

        var baseFetchUrl = StringBuilder("/api/trade/fetch/")

        GlobalScope.launch {
            val resultList = retrofit.getCurrencyExchangeList(
                "api/trade/exchange/Standard", ExchangeCurrencyRequestModel(
                    Exchange(want = selectedCurrencies)
                )
            ).execute().body()

            baseFetchUrl.append(resultList?.result?.subList(0, 20)?.joinToString(separator = ","))
            baseFetchUrl.append("?query=${resultList?.id}")
            baseFetchUrl.append("&exchange")

            val fetchResult =
                retrofit.getCurrencyExchangeResponse(baseFetchUrl.toString()).execute().body()

            println("done")
        }
    }
}