package com.sgorinov.exilehelper.charts_feature.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.sgorinov.exilehelper.charts_feature.R
import com.sgorinov.exilehelper.charts_feature.databinding.ChartsFeatureToolbarBinding
import com.sgorinov.exilehelper.charts_feature.databinding.FragmentOverviewBinding
import com.sgorinov.exilehelper.charts_feature.presentation.ChartsViewModel
import com.sgorinov.exilehelper.charts_feature.presentation.adapters.OverviewAdapter
import com.sgorinov.exilehelper.charts_feature.presentation.models.OverviewViewData
import com.sgorinov.exilehelper.core.presentation.*
import kotlinx.coroutines.flow.collect

internal class OverviewFragment : BaseFragment(R.layout.fragment_overview) {

    private val viewModel by scopedViewModel<ChartsViewModel>(
        FragmentScopes.CHARTS_FEATURE.scopeId,
        FragmentScopes.CHARTS_FEATURE
    )

    private val itemType by lazy {
        requireArguments().getString(CURRENCY_TYPE_KEY, "")
    }
    private val isCurrency by lazy {
        requireArguments().getBoolean(IS_CURRENCY_KEY, false)
    }

    private lateinit var binding: FragmentOverviewBinding
    private lateinit var toolbarLayout: ChartsFeatureToolbarBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentOverviewBinding.bind(view)
        binding = getBinding()

        toolbarLayout = ChartsFeatureToolbarBinding.bind(binding.root)

        val adapter = OverviewAdapter {
            lifecycleScope.launchWhenResumed {
                val data = try {
                    if (isCurrency) {
                        viewModel.getCurrencyHistory(settings.league, itemType, it)
                    } else {
                        viewModel.getItemHistory(settings.league, itemType, it)
                    }
                } catch (e: Exception) {
                    null
                }
                if (data != null) {
                    router.navigateTo(HistoryFragment.newInstance(isCurrency, data))
                }
            }
        }

        toolbarLayout.toolbar.title = "Select currency"
        toolbarLayout.toolbar.setNavigationOnClickListener {
            showMenu()
        }

        binding.overviewList.apply {
            addItemDecoration(
                requireActivity().generateCustomDividerDecoration(
                    R.drawable.colored_list_divider,
                    0
                )
            )
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireActivity())
            this.adapter = adapter
        }

        setupOverviewList(viewModel.overviewData, adapter)

        binding.filterValue.doOnTextChanged { text, _, _, _ ->
            val normalizedConstraint = text?.toString()?.toLowerCaseLocalized()
            if (normalizedConstraint != null) {
                val items = viewModel.overviewData.filter {
                    val isNameContains =
                        it.name.toLowerCaseLocalized().contains(normalizedConstraint)
                    val isTypeContains =
                        it.type?.toLowerCaseLocalized()?.contains(normalizedConstraint) == true
                    isNameContains || isTypeContains
                }
                setupOverviewList(items, adapter)
                binding.overviewList.scrollToPosition(0)
            }
        }

        if (isCurrency) {
            setupTabLayout(savedState.getInt(TAB_POSITION_KEY, 0), adapter)
        } else {
            toolbarLayout.tabLayout.visibility = View.GONE
            adapter.selling = false
        }
        lifecycleScope.launchWhenResumed {
            viewModel.viewLoadingState.collect {
                if (it) {
                    toolbarLayout.toolbarProgressBar.show()
                } else {
                    toolbarLayout.toolbarProgressBar.hide()
                }
            }
        }
    }

    override fun onDestroyView() {
        savedState.putInt(TAB_POSITION_KEY, toolbarLayout.tabLayout.selectedTabPosition)
        binding.overviewList.adapter = null
        super.onDestroyView()
    }

    private fun setupTabLayout(selectedTabPosition: Int, adapter: OverviewAdapter) {
        toolbarLayout.tabLayout.changeTabsFont(
            ResourcesCompat.getFont(
                requireActivity(),
                R.font.fontinsmallcaps
            )
        )
        adapter.selling = selectedTabPosition != 0
        if (toolbarLayout.tabLayout.selectedTabPosition != selectedTabPosition) {
            toolbarLayout.tabLayout.getTabAt(selectedTabPosition)?.let {
                toolbarLayout.tabLayout.selectTab(it, true)
            }
        }

        toolbarLayout.tabLayout.addOnTabSelectedListener(
            TabLayoutListener(
                binding.overviewListContainer,
                binding.overviewList,
                binding.emptyPlaceholder
            ) {
                adapter.selling = it != 0
            }
        )
    }

    private fun setupOverviewList(data: List<OverviewViewData>, adapter: OverviewAdapter) {
        if (data.isEmpty()) {
            binding.emptyPlaceholder.visibility = View.VISIBLE
            binding.overviewList.visibility = View.GONE
        } else {
            binding.emptyPlaceholder.visibility = View.GONE
            binding.overviewList.visibility = View.VISIBLE
            adapter.setData(data)
        }
    }

    companion object {
        private const val CURRENCY_TYPE_KEY = "CURRENCY_TYPE_KEY"
        private const val IS_CURRENCY_KEY = "IS_CURRENCY_KEY"
        private const val TAB_POSITION_KEY = "TAB_POSITION_KEY"

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