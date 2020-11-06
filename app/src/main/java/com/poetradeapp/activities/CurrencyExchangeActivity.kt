package com.poetradeapp.activities

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import coil.ImageLoader
import com.poetradeapp.R
import com.poetradeapp.fragments.PreloadFragment
import com.poetradeapp.fragments.currency.CurrencyExchangeMainFragment
import com.poetradeapp.fragments.currency.CurrencyExchangeResultFragment
import com.poetradeapp.http.RequestService
import com.poetradeapp.models.request.Exchange
import com.poetradeapp.models.request.ExchangeCurrencyRequestModel
import com.poetradeapp.models.response.ExchangeCurrencyResponseModel
import com.poetradeapp.models.view.CurrencyExchangeViewModel
import kotlinx.android.synthetic.main.activity_currency_exchange.*
import kotlinx.android.synthetic.main.fragment_currency_exchange_main.*
import kotlinx.coroutines.*
import org.koin.android.ext.android.get
import retrofit2.await

@ExperimentalCoroutinesApi
class CurrencyExchangeActivity : FragmentActivity() {

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(this.application)
        ).get(CurrencyExchangeViewModel::class.java)
    }

    private val preloadFragment = PreloadFragment()
    private val currencyExchangeMainFragment = CurrencyExchangeMainFragment()
    private val currencyExchangeResultFragment = CurrencyExchangeResultFragment()

    private lateinit var retrofit: RequestService
    lateinit var imageLoader: ImageLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency_exchange)

        retrofit = get()
        imageLoader = get()

        currency_toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.accept -> {
                    resultsLoading.visibility = View.VISIBLE
                    GlobalScope.launch(Dispatchers.Default) {
                        viewModel.setCurrencyResultData(getCurrencyExchangeData())
                    }.invokeOnCompletion {
                        GlobalScope.launch(Dispatchers.Main) {
                            supportFragmentManager
                                .beginTransaction()
                                .add(
                                    R.id.currencyExchangeContainer,
                                    currencyExchangeResultFragment
                                )
                                .hide(currencyExchangeMainFragment)
                                .commit()
                        }
                    }
                    true
                }
                else ->
                    false
            }
        }

        supportFragmentManager
            .beginTransaction()
            .add(R.id.currencyExchangeContainer, preloadFragment)
            .commit()

        GlobalScope.launch(Dispatchers.Main) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.currencyExchangeContainer, currencyExchangeMainFragment)
                .commit()
        }
    }

    override fun onBackPressed() {
        if (currencyExchangeResultFragment.isVisible) {
            supportFragmentManager
                .beginTransaction()
                .remove(currencyExchangeResultFragment)
                .show(currencyExchangeMainFragment)
                .commit()
        } else {
            super.onBackPressed()
        }
    }

    private suspend fun getCurrencyExchangeData(): List<ExchangeCurrencyResponseModel> {
        val baseFetchUrl = StringBuilder("/api/trade/fetch/")

        return withContext(Dispatchers.Default) {
            val resultList = retrofit.getCurrencyExchangeList(
                "api/trade/exchange/Heist", ExchangeCurrencyRequestModel(
                    Exchange(
                        want = viewModel.getWantSelectedCurrencies(),
                        have = viewModel.gethaveSelectedCurrencies()
                    )
                )
            ).await()

            resultList.result.let {
                if (it.size > 20) {
                    baseFetchUrl.append(it.subList(0, 20).joinToString(separator = ","))
                } else {
                    baseFetchUrl.append(it.joinToString(separator = ","))
                }
                baseFetchUrl.append("?query=${resultList.id}")
                baseFetchUrl.append("&exchange")

                retrofit.getCurrencyExchangeResponse(baseFetchUrl.toString()).await().result
            }
        }
    }

    fun closeLoader() {
        supportFragmentManager
            .beginTransaction()
            .remove(preloadFragment)
            .commit()
    }

    fun closeResultsLoader() {
        resultsLoading.visibility = View.GONE
    }
}