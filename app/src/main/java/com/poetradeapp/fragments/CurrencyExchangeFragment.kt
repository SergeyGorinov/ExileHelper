package com.poetradeapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.poetradeapp.R
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_currency_exchange.*

class CurrencyExchangeViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return CurrencyExchangeWant()
            1 -> return CurrencyExchangeHave()
        }
        return CurrencyExchangeWant()
    }
}

class CurrencyExchangeFragment : Fragment() {

    private lateinit var mediator: TabLayoutMediator

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

        currencyExchangeContainer.adapter = CurrencyExchangeViewPagerAdapter(requireActivity())

        mediator = TabLayoutMediator(currencyExchangeTabs, currencyExchangeContainer) { tab, pos ->
            when (pos) {
                0 -> tab.text = "Want"
                1 -> tab.text = "Have"
            }
        }
    }
}