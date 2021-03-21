package com.poe.tradeapp.charts_feature.presentation.fragments

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.poe.tradeapp.charts_feature.R
import com.poe.tradeapp.charts_feature.databinding.FragmentCurrenciesOverviewBinding
import com.poe.tradeapp.charts_feature.presentation.ChartsViewModel
import com.poe.tradeapp.charts_feature.presentation.adapters.CurrencyOverviewAdapter
import com.poe.tradeapp.charts_feature.presentation.adapters.ItemOverviewAdapter
import com.poe.tradeapp.core.presentation.BaseFragment
import com.poe.tradeapp.core.presentation.generateLinearDividerDecoration
import com.poe.tradeapp.core.presentation.getTransparentProgressDialog
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class OverviewFragment : BaseFragment(R.layout.fragment_currencies_overview) {

    private val viewModel by sharedViewModel<ChartsViewModel>()

    private val itemType by lazy {
        requireArguments().getString(CURRENCY_TYPE_KEY, "")
    }
    private val isCurrency by lazy {
        requireArguments().getBoolean(IS_CURRENCY_KEY, false)
    }

    private lateinit var binding: FragmentCurrenciesOverviewBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val progressDialog = requireActivity().getTransparentProgressDialog()

        viewBinding = FragmentCurrenciesOverviewBinding.bind(view)
        binding = getBinding()

        binding.toolbarLayout.toolbar.title = "Select currency"
        binding.currenciesOverview.addItemDecoration(requireActivity().generateLinearDividerDecoration())
        binding.currenciesOverview.layoutManager = LinearLayoutManager(requireActivity())
        lifecycleScope.launchWhenResumed {
            if (isCurrency) {
                val items = viewModel.getCurrenciesOverview(settings.league, itemType)
                binding.currenciesOverview.adapter = CurrencyOverviewAdapter(
                    items,
                    false,
                    {
                        router.navigateTo(HistoryFragment.newInstance(it, itemType, isCurrency))
                    },
                    {
                        createWikiDialog(it).show()
                    }
                )
            } else {
                val items = viewModel.getItemsOverview(settings.league, itemType)
                binding.currenciesOverview.adapter = ItemOverviewAdapter(items) {
                    router.navigateTo(HistoryFragment.newInstance(it, itemType, isCurrency))
                }
            }
        }
        lifecycleScope.launchWhenCreated {
            viewModel.viewLoadingState.collect {
                if (it) {
                    progressDialog.show()
                } else {
                    progressDialog.dismiss()
                }
            }
        }
    }

    private fun createWikiDialog(path: String): AlertDialog {
        return AlertDialog.Builder(requireActivity())
            .setPositiveButton("Yes") { _, _ ->
                val uri = Uri.Builder()
                    .scheme("https")
                    .authority("pathofexile.gamepedia.com")
                    .appendPath(path)
                    .build()
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = uri
                }
                requireActivity().startActivity(intent)
            }
            .setNegativeButton("No") { _, _ ->
                return@setNegativeButton
            }
            .setTitle("Open page on PoeWiki?")
            .create()
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