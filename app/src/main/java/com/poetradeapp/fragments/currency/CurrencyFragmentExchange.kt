package com.poetradeapp.fragments.currency

import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.poetradeapp.R
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_currency_exchange.*

class CurrencyFragmentExchange : Fragment() {

    private lateinit var mediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflanter = TransitionInflater.from(requireContext())
        exitTransition = inflanter.inflateTransition(R.transition.fragment_fade)
        enterTransition = inflanter.inflateTransition(R.transition.fragment_fade)
    }

    override fun onPause() {
        mediator.detach()
        super.onPause()
    }

    override fun onResume() {
        mediator.attach()
        super.onResume()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_currency_exchange, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currencyExchangeMainContainer.offscreenPageLimit = 2
        currencyExchangeMainContainer.adapter = CurrencyExchangeViewPagerAdapter(requireActivity())

        mediator =
            TabLayoutMediator(currencyExchangeTabs, currencyExchangeMainContainer) { tab, pos ->
                when (pos) {
                    0 -> tab.text = "Want"
                    1 -> tab.text = "Have"
                }
            }
    }
}