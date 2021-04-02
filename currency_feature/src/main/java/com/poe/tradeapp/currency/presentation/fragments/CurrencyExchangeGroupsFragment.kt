package com.poe.tradeapp.currency.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.poe.tradeapp.core.presentation.BaseFragment
import com.poe.tradeapp.core.presentation.FragmentScopes
import com.poe.tradeapp.core.presentation.scopedViewModel
import com.poe.tradeapp.currency.R
import com.poe.tradeapp.currency.databinding.FragmentCurrencyExchangeGroupsBinding
import com.poe.tradeapp.currency.presentation.CurrencyExchangeViewModel
import com.poe.tradeapp.currency.presentation.adapters.CurrencyGroupsAdapter

internal class CurrencyExchangeGroupsFragment :
    BaseFragment(R.layout.fragment_currency_exchange_groups) {

    private val viewModel by scopedViewModel<CurrencyExchangeViewModel>(
        FragmentScopes.CURRENCY_FEATURE.scopeId,
        FragmentScopes.CURRENCY_FEATURE
    )

    private val isFromWanted by lazy {
        arguments?.getBoolean(IS_FROM_WANTED_KEY, false) ?: false
    }

    private lateinit var binding: FragmentCurrencyExchangeGroupsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = CurrencyGroupsAdapter(viewModel.currencyItems) {
            if (it == "Maps") {
                router.navigateTo(
                    CurrencyExchangeMapsFragment.newInstance(isFromWanted)
                )
            } else {
                router.navigateTo(
                    CurrencyExchangeGroupFragment.newInstance(it, isFromWanted)
                )
            }
        }
        viewBinding = FragmentCurrencyExchangeGroupsBinding.bind(view)
        binding = getBinding()
        binding.toolbarLayout.toolbar.title = "Select currency group"
        binding.groups.addItemDecoration(
            DividerItemDecoration(
                requireActivity(),
                DividerItemDecoration.VERTICAL
            ).apply {
                ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.recyclerview_divider
                )?.let {
                    setDrawable(it)
                }
            })
        binding.groups.layoutManager = LinearLayoutManager(requireActivity())
        binding.groups.adapter = adapter
    }

    companion object {
        private const val IS_FROM_WANTED_KEY = "IS_FROM_WANTED_KEY"

        fun newInstance(isFromWant: Boolean) = FragmentScreen {
            CurrencyExchangeGroupsFragment().apply {
                arguments = bundleOf(IS_FROM_WANTED_KEY to isFromWant)
            }
        }
    }
}