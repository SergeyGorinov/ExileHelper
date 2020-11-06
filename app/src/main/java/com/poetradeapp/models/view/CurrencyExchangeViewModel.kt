package com.poetradeapp.models.view

import androidx.lifecycle.ViewModel
import com.poetradeapp.models.response.ExchangeCurrencyResponseModel

class CurrencyExchangeViewModel : ViewModel() {

    private val wantSelectedCurrencies = mutableListOf<String>()
    private val haveSelectedCurrencies = mutableListOf<String>()

    private var currencyResultData: List<ExchangeCurrencyResponseModel>? = null

    fun getWantSelectedCurrencies() = wantSelectedCurrencies
    fun gethaveSelectedCurrencies() = haveSelectedCurrencies

    fun setCurrencyResultData(data: List<ExchangeCurrencyResponseModel>) {
        currencyResultData = data
    }

    fun getCurrencyResultData() = currencyResultData ?: listOf()

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
}