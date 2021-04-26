package com.sgorinov.exilehelper.currency_feature.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.sgorinov.exilehelper.core.presentation.BaseFragment
import com.sgorinov.exilehelper.core.presentation.FragmentScopes
import com.sgorinov.exilehelper.core.presentation.scopedViewModel
import com.sgorinov.exilehelper.currency_feature.R
import com.sgorinov.exilehelper.currency_feature.databinding.FragmentCurrencyExchangeMapsBinding
import com.sgorinov.exilehelper.currency_feature.presentation.CurrencyExchangeViewModel
import com.sgorinov.exilehelper.currency_feature.presentation.adapters.CurrencyGroupsAdapter

internal class CurrencyExchangeMapsFragment :
    BaseFragment(R.layout.fragment_currency_exchange_maps) {

    private val viewModel by scopedViewModel<CurrencyExchangeViewModel>(
        FragmentScopes.CURRENCY_FEATURE.scopeId,
        FragmentScopes.CURRENCY_FEATURE
    )

    private val isFromWanted by lazy {
        arguments?.getBoolean(IS_FROM_WANTED_KEY, false) ?: false
    }

    private lateinit var binding: FragmentCurrencyExchangeMapsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding = FragmentCurrencyExchangeMapsBinding.bind(view)
        binding = getBinding()
        binding.toolbarLayout.toolbar.title = "Select map tier"
        binding.toolbarLayout.toolbar.setNavigationOnClickListener {
            showMenu()
        }
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