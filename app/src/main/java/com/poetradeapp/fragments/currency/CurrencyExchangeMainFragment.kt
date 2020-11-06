package com.poetradeapp.fragments.currency

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.poetradeapp.R
import com.poetradeapp.models.ui.StaticGroupViewData
import kotlinx.android.synthetic.main.fragment_currency_exchange_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class CurrencyExchangeViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    private val items = arrayListOf<StaticGroupViewData>()

    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return CurrencyExchangeWantFragment(items)
            1 -> return CurrencyExchangeHaveFragment(items)
        }
        return CurrencyExchangeWantFragment(items)
    }
}

@ExperimentalCoroutinesApi
class CurrencyExchangeMainFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflanter = TransitionInflater.from(requireContext())
        exitTransition = inflanter.inflateTransition(R.transition.fragment_slide)
        enterTransition = inflanter.inflateTransition(R.transition.fragment_slide)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_currency_exchange_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currencyExchangeMainContainer.offscreenPageLimit = 2
        currencyExchangeMainContainer.adapter = CurrencyExchangeViewPagerAdapter(requireActivity())

        TabLayoutMediator(currencyExchangeTabs, currencyExchangeMainContainer) { tab, pos ->
            when (pos) {
                0 -> tab.text = "Want"
                1 -> tab.text = "Have"
            }
        }.attach()
    }
}