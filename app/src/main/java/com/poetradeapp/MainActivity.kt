package com.example.poetradeapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.poetradeapp.adapters.CurrencyListAdapter
import com.poetradeapp.http.RequestService
import com.poetradeapp.models.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var retrofit: RequestService

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(this.application)
        ).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        retrofit = RequestService.create("https://www.pathofexile.com/")

         runBlocking {
             viewModel.setMainData(getCurrencyData())
        }

        testButton.setOnClickListener {
            viewModel.sendCurrencyExchangeRequest(retrofit)
        }

        currencyList.layoutManager = LinearLayoutManager(this)
        currencyList.adapter = CurrencyListAdapter(viewModel.getMainData()?.subList(3, 4) ?: listOf(), this, retrofit)
    }

    private suspend fun getCurrencyData() = run {
        withContext(Dispatchers.Default) {
            retrofit.getStaticData("api/trade/data/static").execute()
                .body()?.result as List<StaticEntries>
        }
    }
}
