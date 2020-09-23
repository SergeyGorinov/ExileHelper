package com.poetradeapp.models

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.LruCache
import androidx.lifecycle.ViewModel
import com.poetradeapp.RealmCurrencyGroupData
import com.poetradeapp.http.RequestService
import io.realm.Realm
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {

    private val currencyData: List<RealmCurrencyGroupData> =
        Realm.getDefaultInstance()
            .where(RealmCurrencyGroupData::class.java)
            .findAll()
            .toList()
    private val wantSelectedCurrencies = mutableListOf<String>()
    private val haveSelectedCurrencies = mutableListOf<String>()
    private var currencyResultData: ExchangeCurrencyResponse? = null
    private val currencyIcons: MutableMap<String, Drawable> = mutableMapOf()

    fun initializeIcons(context: Context) {
        currencyData.forEach { group ->
            group.currencies.forEach { currency ->
                currency.image?.let {
                    currencyIcons[currency.id] = BitmapDrawable(
                        context.resources,
                        BitmapFactory.decodeByteArray(it, 0, it.size)
                    )
                }
            }
        }
    }

    fun getMainData() = currencyData
    fun getCurrencyResultsData() = currencyResultData
    fun getCurrencyIcon(id: String): Drawable? = currencyIcons[id]

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

            resultList?.result?.let {
                if (it.size > 20) {
                    baseFetchUrl.append(it.subList(0, 20).joinToString(separator = ","))
                } else {
                    baseFetchUrl.append(it.joinToString(separator = ","))
                }
                baseFetchUrl.append("?query=${resultList.id}")
                baseFetchUrl.append("&exchange")

                retrofit.getCurrencyExchangeResponse(baseFetchUrl.toString()).execute().body()
            }
        }
    }
}