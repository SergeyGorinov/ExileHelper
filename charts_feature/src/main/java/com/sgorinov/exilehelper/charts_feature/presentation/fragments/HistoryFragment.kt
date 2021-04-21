package com.sgorinov.exilehelper.charts_feature.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.google.android.material.tabs.TabLayoutMediator
import com.sgorinov.exilehelper.charts_feature.R
import com.sgorinov.exilehelper.charts_feature.databinding.FragmentHistoryBinding
import com.sgorinov.exilehelper.charts_feature.databinding.HistoryFragmentToolbarBinding
import com.sgorinov.exilehelper.charts_feature.presentation.adapters.HistoryViewPagerAdapter
import com.sgorinov.exilehelper.charts_feature.presentation.models.HistoryModel
import com.sgorinov.exilehelper.core.presentation.BaseFragment
import com.sgorinov.exilehelper.core.presentation.changeTabsFont

internal class HistoryFragment : BaseFragment(R.layout.fragment_history) {

    private val isCurrency by lazy { requireArguments().getBoolean(IS_CURRENCY_KEY, false) }

    internal var data: HistoryModel? = null

    private lateinit var binding: FragmentHistoryBinding
    private lateinit var toolbarLayout: HistoryFragmentToolbarBinding

    private var tabLayoutMediator: TabLayoutMediator? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (data == null) {
            router.exit()
            return
        }

        viewBinding = FragmentHistoryBinding.bind(view)
        binding = getBinding()
        toolbarLayout = HistoryFragmentToolbarBinding.bind(binding.root)

        tabLayoutMediator = TabLayoutMediator(
            toolbarLayout.tabLayout,
            binding.container
        ) { tab, pos ->
            when (pos) {
                0 -> tab.text = getString(R.string.info)
                1 -> tab.text = getString(R.string.charts)
            }
        }

        toolbarLayout.toolbar.title = "Item history"
        toolbarLayout.toolbar.setNavigationOnClickListener {
            showMenu()
        }

        data?.let {
            binding.container.adapter = HistoryViewPagerAdapter(
                it,
                isCurrency
            ) { isCurrencyExchange, withNotification, itemData1, itemData2 ->
                if (isCurrencyExchange) {
                    getMainActivity()?.goToCurrencyExchange(itemData1, itemData2, withNotification)
                } else {
                    getMainActivity()?.goToItemsSearch(itemData1, itemData2, withNotification)
                }
            }
            tabLayoutMediator?.attach()
            toolbarLayout.tabLayout.changeTabsFont(
                ResourcesCompat.getFont(
                    requireActivity(),
                    R.font.fontinsmallcaps
                )
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        data = null
        tabLayoutMediator = null
    }

    companion object {
        private const val IS_CURRENCY_KEY = "IS_CURRENCY_KEY"

        fun newInstance(isCurrency: Boolean, data: HistoryModel): FragmentScreen {
            return FragmentScreen {
                HistoryFragment().apply {
                    this.data = data
                    arguments = bundleOf(IS_CURRENCY_KEY to isCurrency)
                }
            }
        }
    }
}