package com.example.poetradeapp

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayoutStates
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.poetradeapp.fragments.CurrencyExchangeFragment
import com.poetradeapp.fragments.CurrencyResultFragment
import com.poetradeapp.fragments.ItemExchangeFragment
import com.poetradeapp.http.RequestService
import com.poetradeapp.models.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.consumeEach

class SwipePagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return CurrencyExchangeFragment()
            1 -> return ItemExchangeFragment()
        }
        return CurrencyExchangeFragment()
    }
}

class MainActivity : FragmentActivity() {

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

        fragmentContainer.adapter = SwipePagerAdapter(this)

//        supportFragmentManager.beginTransaction()
//            .add(R.id.fragmentContainer, currencyExchangeFragment)
//            .commit()

//        GlobalScope.launch {
//            viewModel.channel.consumeEach {
//                supportFragmentManager.beginTransaction()
//                    .hide(mainFragment)
//                    .show(currencyResultFragment)
//                    .commit()
//            }
//        }

        retrofit = RequestService.create("https://www.pathofexile.com/")

        //Loading...
        runBlocking {
            viewModel.setMainData(getCurrencyData())
        }

//
//        testButton.setOnClickListener {
//            viewModel.sendCurrencyExchangeRequest(retrofit)
//        }
//
//        currencyList.layoutManager = LinearLayoutManager(this)
//        currencyList.adapter =
//            CurrencyListAdapter(viewModel.getMainData()?.subList(0, 5) ?: listOf(), this, retrofit)
    }

    private suspend fun getCurrencyData() = run {
        withContext(Dispatchers.Default) {
            retrofit.getStaticData("api/trade/data/static").execute().body()?.let {
                it.result
            } ?: listOf()
        }
    }
}
