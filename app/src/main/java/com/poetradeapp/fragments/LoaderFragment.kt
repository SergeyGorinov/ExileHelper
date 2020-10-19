package com.poetradeapp.fragments

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.poetradeapp.PoeTradeApplication
import com.poetradeapp.R
import com.poetradeapp.activities.MainActivity
import com.poetradeapp.helpers.CoilImageLoader
import com.poetradeapp.helpers.StaticDataLoader
import com.poetradeapp.http.RequestService
import com.poetradeapp.models.CurrencyGroupViewData
import com.poetradeapp.models.CurrencyViewData
import kotlinx.android.synthetic.main.fragment_loader.*
import kotlinx.coroutines.*
import retrofit2.await
import javax.inject.Inject

@ExperimentalCoroutinesApi
class LoaderFragment : Fragment() {

    @Inject
    lateinit var imageLoader: CoilImageLoader

    @Inject
    lateinit var retrofit: RequestService

    @Inject
    lateinit var staticDataInstance: StaticDataLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (requireActivity().application as PoeTradeApplication).getDaggerComponent().inject(this)

        val transitionInflanter = TransitionInflater.from(context)
        enterTransition = transitionInflanter.inflateTransition(R.transition.fragment_fade)
        exitTransition = transitionInflanter.inflateTransition(R.transition.fragment_fade)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_loader, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loaderProgressBar.progress = 0

        GlobalScope.launch {
            downloadData()
        }.invokeOnCompletion {
            GlobalScope.launch(Dispatchers.Main) {
                (context as MainActivity).closeLoader()
            }
        }
    }

    private suspend fun downloadData() {

        val leaguesDataResponse =
            retrofit.getService().getLeagueData("api/trade/data/leagues").await()
        val itemsDataResponse =
            retrofit.getService().getItemsData("api/trade/data/items").await()
        val statsDataResponse =
            retrofit.getService().getStatsData("api/trade/data/stats").await()
        val currenciesDataResponse =
            retrofit.getService().getCurrencyData("api/trade/data/static").await()

        val allEntriesCount =
            leaguesDataResponse.result.count() +
                    itemsDataResponse.result.sumBy { sum -> sum.entries.count() } +
                    statsDataResponse.result.count() +
                    currenciesDataResponse.result.sumBy { sum -> sum.entries.count() }

        val mapsGroup = CurrencyGroupViewData("Maps", "All Maps")

        withContext(Dispatchers.Main) {
            loaderProgressBar.max = allEntriesCount
        }

        leaguesDataResponse.result.forEach { league ->
            staticDataInstance.addLeagueData(league)
            withContext(Dispatchers.Main) {
                loaderProgressBar.incrementProgressBy(1)
            }
        }

        itemsDataResponse.result.forEach { items ->
            staticDataInstance.addItemsData(items)
            withContext(Dispatchers.Main) {
                loaderProgressBar.incrementProgressBy(1)
            }
        }

        statsDataResponse.result.forEach { stats ->
            staticDataInstance.addStatsData(stats)
            withContext(Dispatchers.Main) {
                loaderProgressBar.incrementProgressBy(1)
            }
        }

        currenciesDataResponse.result.forEach { currencyGroupData ->
            val currencies = arrayListOf<CurrencyViewData>()
            currencyGroupData.entries.forEach { currencyData ->
                val currency = CurrencyViewData(
                    currencyData.id,
                    currencyData.text,
                    currencyData.image,
                    currencyGroupData.label
                )
                currencies.add(currency)
                withContext(Dispatchers.Main) {
                    loaderProgressBar.incrementProgressBy(1)
                }
            }
            if (currencies.size > 0) {
                if (currencyGroupData.id.startsWith("Maps")) {
                    mapsGroup.currencies.addAll(currencies)
                } else {
                    staticDataInstance.addCurrencyData(
                        CurrencyGroupViewData(
                            currencyGroupData.id,
                            currencyGroupData.label,
                            currencies
                        )
                    )
                }
            }
        }
        staticDataInstance.addCurrencyData(mapsGroup)
    }
}