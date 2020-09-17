package com.example.poetradeapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.flexbox.*
import com.poetradeapp.adapters.CurrencyGroupAdapter
import com.poetradeapp.adapters.CurrencyListAdapter
import com.poetradeapp.http.RequestService
import com.poetradeapp.models.MainViewModel
import com.poetradeapp.models.StaticData
import com.poetradeapp.models.StaticEntries
import com.poetradeapp.ui.SlideUpDownAnimator
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

        val staticData = runBlocking {
            getCurrencyData()
        }

        currencyList.layoutManager = LinearLayoutManager(this)
        currencyList.adapter = CurrencyListAdapter(staticData.subList(0, 2), this, retrofit)
    }

    private suspend fun getCurrencyData() = run {
        withContext(Dispatchers.Default) {
            retrofit.getStaticData("api/trade/data/static").execute()
                .body()?.result as List<StaticEntries>
        }
    }
}
