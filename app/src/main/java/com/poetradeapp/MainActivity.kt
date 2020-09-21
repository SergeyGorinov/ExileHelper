package com.example.poetradeapp

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.poetradeapp.fragments.CurrencyResultFragment
import com.poetradeapp.fragments.MainFragment
import com.poetradeapp.http.RequestService
import com.poetradeapp.models.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.consumeEach

class SwipePagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return MainFragment()
            1 -> return CurrencyResultFragment()
        }
        return MainFragment()
    }
}

class SwipePagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

class MainActivity : FragmentActivity() {

    private lateinit var retrofit: RequestService

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(this.application)
        ).get(MainViewModel::class.java)
    }

    private val mainFragment = MainFragment()
    private val currencyResultFragment = CurrencyResultFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        viewPager.adapter = SwipePagerAdapter(this)

        supportFragmentManager.beginTransaction()
            .add(R.id.mainFragment, mainFragment)
            .add(R.id.mainFragment, currencyResultFragment)
            .hide(currencyResultFragment)
            .commit()

        GlobalScope.launch {
            viewModel.channel.consumeEach {
                supportFragmentManager.beginTransaction()
                    .hide(mainFragment)
                    .show(currencyResultFragment)
                    .commit()
            }
        }

//        retrofit = RequestService.create("https://www.pathofexile.com/")
//
//        runBlocking {
//            viewModel.setMainData(getCurrencyData())
//        }
//
//        testButton.setOnClickListener {
//            viewModel.sendCurrencyExchangeRequest(retrofit)
//        }
//
//        currencyList.layoutManager = LinearLayoutManager(this)
//        currencyList.adapter =
//            CurrencyListAdapter(viewModel.getMainData()?.subList(0, 5) ?: listOf(), this, retrofit)
    }

    override fun onBackPressed() {
        if (currencyResultFragment.isVisible) {
            supportFragmentManager.beginTransaction()
                .hide(currencyResultFragment)
                .show(mainFragment)
                .commit()
        }
    }

    private suspend fun getCurrencyData() = run {
        withContext(Dispatchers.Default) {
            retrofit.getStaticData("api/trade/data/static").execute().body()?.let {
                it.result
            } ?: listOf()
        }
    }
}
