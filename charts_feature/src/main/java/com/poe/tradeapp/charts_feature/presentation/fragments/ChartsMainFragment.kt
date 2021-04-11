package com.poe.tradeapp.charts_feature.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.poe.tradeapp.charts_feature.R
import com.poe.tradeapp.charts_feature.databinding.FragmentChartsMainBinding
import com.poe.tradeapp.charts_feature.presentation.ChartsViewModel
import com.poe.tradeapp.charts_feature.presentation.adapters.ItemsGroupsAdapter
import com.poe.tradeapp.core.presentation.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ChartsMainFragment : BaseFragment(R.layout.fragment_charts_main) {

    private val scope by fragmentLifecycleScope(
        FragmentScopes.CHARTS_FEATURE.scopeId,
        FragmentScopes.CHARTS_FEATURE
    )

    private val viewModel by scopedViewModel<ChartsViewModel>(
        FragmentScopes.CHARTS_FEATURE.scopeId,
        FragmentScopes.CHARTS_FEATURE
    )

    private lateinit var binding: FragmentChartsMainBinding

    private var fetchingOverviewJob: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentChartsMainBinding.bind(view)
        binding = getBinding()

        binding.toolbarLayout.toolbar.title = "Select items group"
        binding.groups.addItemDecoration(requireActivity().generateLinearDividerDecoration())
        binding.groups.layoutManager = LinearLayoutManager(requireActivity())
        binding.groups.adapter = ItemsGroupsAdapter(
            viewModel.getItemsGroups()
        ) { type, isCurrency ->
            if (fetchingOverviewJob?.isActive == true) {
                return@ItemsGroupsAdapter
            }
            fetchingOverviewJob = lifecycleScope.launch {
                if (isCurrency) {
                    viewModel.getCurrenciesOverview(settings.league, type)
                } else {
                    viewModel.getItemsOverview(settings.league, type)
                }
                router.navigateTo(OverviewFragment.newInstance(type, isCurrency))
            }
        }
        lifecycleScope.launchWhenResumed {
            viewModel.viewLoadingState.collect {
                if (it) {
                    binding.toolbarLayout.toolbarProgressBar.show()
                } else {
                    binding.toolbarLayout.toolbarProgressBar.hide()
                }
            }
        }
    }

    override fun onDestroyView() {
        binding.groups.adapter = null
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.close()
    }

    companion object {
        fun newInstance() = FragmentScreen { ChartsMainFragment() }
    }
}
