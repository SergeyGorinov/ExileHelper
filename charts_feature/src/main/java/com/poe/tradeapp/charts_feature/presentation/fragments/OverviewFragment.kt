package com.poe.tradeapp.charts_feature.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.poe.tradeapp.charts_feature.R
import com.poe.tradeapp.charts_feature.databinding.FragmentOverviewBinding
import com.poe.tradeapp.charts_feature.presentation.ChartsViewModel
import com.poe.tradeapp.charts_feature.presentation.adapters.OverviewAdapter
import com.poe.tradeapp.core.presentation.BaseFragment
import com.poe.tradeapp.core.presentation.HeaderItemDecoration
import com.poe.tradeapp.core.presentation.generateLinearDividerDecoration
import com.poe.tradeapp.core.presentation.getTransparentProgressDialog
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

internal class OverviewFragment : BaseFragment(R.layout.fragment_overview) {

    private val viewModel by sharedViewModel<ChartsViewModel>()

    private val itemType by lazy {
        requireArguments().getString(CURRENCY_TYPE_KEY, "")
    }
    private val isCurrency by lazy {
        requireArguments().getBoolean(IS_CURRENCY_KEY, false)
    }

    private lateinit var binding: FragmentOverviewBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentOverviewBinding.bind(view)
        binding = getBinding()

        val progressDialog = requireActivity().getTransparentProgressDialog()

        val adapter = OverviewAdapter {
            lifecycleScope.launchWhenResumed {
                if (isCurrency) {
                    viewModel.getCurrencyHistory(settings.league, itemType, it)
                } else {
                    viewModel.getItemHistory(settings.league, itemType, it)
                }
                router.navigateTo(HistoryFragment.newInstance(isCurrency))
            }
        }

        if (!isCurrency) {
            binding.buttonPanel.visibility = View.GONE
        } else {
            binding.showBuying.setOnClickListener {
                adapter.selling = false
            }
            binding.showSelling.setOnClickListener {
                adapter.selling = true
            }
        }

        binding.toolbarLayout.toolbar.title = "Select currency"

        binding.currenciesOverview.apply {
            addItemDecoration(requireActivity().generateLinearDividerDecoration())
            addItemDecoration(HeaderItemDecoration(R.layout.overview_header))
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireActivity())
            binding.currenciesOverview.adapter = adapter
        }
        lifecycleScope.launchWhenResumed {
            if (isCurrency) {
                viewModel.getCurrenciesOverview(settings.league, itemType)
            } else {
                adapter.selling = false
                viewModel.getItemsOverview(settings.league, itemType)
            }
            adapter.setData(viewModel.overviewData[itemType] ?: listOf())
        }
        lifecycleScope.launchWhenResumed {
            viewModel.viewLoadingState.collect {
                if (it) {
                    progressDialog.show()
                } else {
                    progressDialog.dismiss()
                }
            }
        }
    }

    companion object {
        private const val CURRENCY_TYPE_KEY = "CURRENCY_TYPE_KEY"
        private const val IS_CURRENCY_KEY = "IS_CURRENCY_KEY"

        fun newInstance(currencyType: String, isCurrency: Boolean): FragmentScreen {
            return FragmentScreen {
                OverviewFragment().apply {
                    arguments = bundleOf(
                        CURRENCY_TYPE_KEY to currencyType,
                        IS_CURRENCY_KEY to isCurrency
                    )
                }
            }
        }
    }
}