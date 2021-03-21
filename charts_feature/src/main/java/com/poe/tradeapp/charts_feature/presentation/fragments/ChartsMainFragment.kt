package com.poe.tradeapp.charts_feature.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.poe.tradeapp.charts_feature.R
import com.poe.tradeapp.charts_feature.databinding.FragmentChartsMainBinding
import com.poe.tradeapp.charts_feature.presentation.ChartsViewModel
import com.poe.tradeapp.charts_feature.presentation.adapters.ItemsGroupsAdapter
import com.poe.tradeapp.core.presentation.BaseFragment
import com.poe.tradeapp.core.presentation.generateLinearDividerDecoration
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ChartsMainFragment : BaseFragment(R.layout.fragment_charts_main) {

    private val viewModel by sharedViewModel<ChartsViewModel>()

    private lateinit var binding: FragmentChartsMainBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentChartsMainBinding.bind(view)
        binding = getBinding()

        binding.toolbarLayout.toolbar.title = "Select items group"
        binding.groups.addItemDecoration(requireActivity().generateLinearDividerDecoration())
        binding.groups.layoutManager = LinearLayoutManager(requireActivity())
        binding.groups.adapter =
            ItemsGroupsAdapter(viewModel.getItemsGroups()) { type, isCurrency ->
                router.navigateTo(OverviewFragment.newInstance(type, isCurrency))
            }
    }

    companion object {
        fun newInstance() = FragmentScreen { ChartsMainFragment() }
    }
}
