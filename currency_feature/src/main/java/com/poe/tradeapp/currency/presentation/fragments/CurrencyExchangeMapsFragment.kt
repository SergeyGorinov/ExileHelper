package com.poe.tradeapp.currency.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.poe.tradeapp.core.presentation.BaseFragment
import com.poe.tradeapp.currency.R
import com.poe.tradeapp.currency.databinding.FragmentCurrencyExchangeMapsBinding
import com.poe.tradeapp.currency.presentation.CurrencyExchangeViewModel
import com.poe.tradeapp.currency.presentation.adapters.CurrencyGroupsAdapter
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

internal class CurrencyExchangeMapsFragment :
    BaseFragment(R.layout.fragment_currency_exchange_maps) {

    private val isFromWanted by lazy {
        arguments?.getBoolean(IS_FROM_WANTED_KEY, false) ?: false
    }

    private val viewModel by sharedViewModel<CurrencyExchangeViewModel>()

    private lateinit var binding: FragmentCurrencyExchangeMapsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding = FragmentCurrencyExchangeMapsBinding.bind(view)
        binding = getBinding()
        binding.toolbarLayout.toolbar.title = "Select map tier"
        binding.mapTiers.layoutManager = LinearLayoutManager(requireActivity())
        binding.mapTiers.addItemDecoration(
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
        val adapter = CurrencyGroupsAdapter(viewModel.maps) {
            router.navigateTo(
                CurrencyExchangeGroupFragment.newInstance(it, isFromWanted)
            )
        }
        binding.mapTiers.adapter = adapter
    }

    companion object {
        private const val IS_FROM_WANTED_KEY = "IS_FROM_WANTED_KEY"

        fun newInstance(isFromWant: Boolean) = FragmentScreen {
            CurrencyExchangeMapsFragment().apply {
                arguments = bundleOf(IS_FROM_WANTED_KEY to isFromWant)
            }
        }
    }
}