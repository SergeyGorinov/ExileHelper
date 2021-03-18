package com.poe.tradeapp.exchange.presentation.fragments

import android.os.Bundle
import android.view.View
import com.google.android.material.tabs.TabLayoutMediator
import com.poe.tradeapp.core.presentation.BaseFragment
import com.poe.tradeapp.exchange.R
import com.poe.tradeapp.exchange.databinding.FragmentItemExchangeMainBinding
import com.poe.tradeapp.exchange.presentation.adapters.ItemsSearchViewPagerAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class ItemsSearchMainFragment : BaseFragment(R.layout.fragment_item_exchange_main) {

    private lateinit var binding: FragmentItemExchangeMainBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding = FragmentItemExchangeMainBinding.bind(view)
        binding = getBinding()

        binding.itemsSearchMainContainer.adapter =
            ItemsSearchViewPagerAdapter(requireActivity())
        TabLayoutMediator(
            binding.itemsSearchTabs,
            binding.itemsSearchMainContainer
        ) { tab, pos ->
            when (pos) {
                0 -> tab.text = "Filters"
            }
        }.attach()
    }

    fun showResultsLoader() {
        binding.resultsLoading.visibility = View.VISIBLE
    }

    fun closeResultsLoader() {
        binding.resultsLoading.visibility = View.GONE
    }
}