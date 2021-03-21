package com.poe.tradeapp.currency.presentation.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.poe.tradeapp.core.presentation.BaseFragment
import com.poe.tradeapp.core.presentation.generateFlexboxDecorator
import com.poe.tradeapp.core.presentation.generateFlexboxManager
import com.poe.tradeapp.core.presentation.getTransparentProgressDialog
import com.poe.tradeapp.currency.R
import com.poe.tradeapp.currency.databinding.FragmentCurrencyExchangeMainBinding
import com.poe.tradeapp.currency.presentation.CurrencyExchangeViewModel
import com.poe.tradeapp.currency.presentation.adapters.CurrencySelectedAdapter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class CurrencyExchangeMainFragment : BaseFragment(R.layout.fragment_currency_exchange_main) {

    private val viewModel by sharedViewModel<CurrencyExchangeViewModel>()

    private lateinit var binding: FragmentCurrencyExchangeMainBinding

    private val wantItemId by lazy { requireArguments().getString(WANT_ITEM_ID_KEY) }
    private val haveItemId by lazy { requireArguments().getString(HAVE_ITEM_ID_KEY) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            viewModel.requestItems()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val progressDialog = requireActivity().getTransparentProgressDialog()

        lifecycleScope.launchWhenCreated {
            viewModel.viewLoadingState.collect {
                if (it) {
                    progressDialog.show()
                } else {
                    progressDialog.dismiss()
                }
            }
        }

        viewBinding = FragmentCurrencyExchangeMainBinding.bind(view)
        binding = getBinding()

        binding.toolbarLayout.toolbar.inflateMenu(R.menu.menu_currency)
        binding.toolbarLayout.toolbar.title = "Currency Exchange"
        binding.toolbarLayout.toolbar.setNavigationOnClickListener {
            showMenu()
        }
        binding.toolbarLayout.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.accept -> {
                    requestResults()
                    true
                }
                else ->
                    false
            }
        }
        binding.wantAddCurrency.setOnClickListener {
            router.navigateTo(CurrencyExchangeGroupsFragment.newInstance(true))
        }
        binding.haveAddCurrency.setOnClickListener {
            router.navigateTo(CurrencyExchangeGroupsFragment.newInstance(false))
        }
        wantItemId?.let {
            viewModel.wantCurrencies.clear()
            viewModel.wantCurrencies.add(it)
        }
        haveItemId?.let {
            viewModel.haveCurrencies.clear()
            viewModel.haveCurrencies.add(it)
        }
        restoreState()
        if (wantItemId != null) {
            requestResults()
            requireArguments().clear()
        }
    }

    private fun restoreState() {
        val wantItems = viewModel.wantCurrencies.map { selectedId ->
            viewModel.allCurrencies.flatMap { it.staticItems }.filter { it.id == selectedId }
        }.flatten().toMutableList()
        val haveItems = viewModel.haveCurrencies.map { selectedId ->
            viewModel.allCurrencies.flatMap { it.staticItems }.filter { it.id == selectedId }
        }.flatten().toMutableList()

        binding.wantCurrencies.layoutManager = requireActivity().generateFlexboxManager()
        binding.wantCurrencies.adapter = CurrencySelectedAdapter(wantItems) { currencyId ->
            wantItems.removeAll { it.id == currencyId }
            viewModel.wantCurrencies.remove(currencyId)
        }
        binding.wantCurrencies.addItemDecoration(requireActivity().generateFlexboxDecorator())
        binding.haveCurrencies.layoutManager = requireActivity().generateFlexboxManager()
        binding.haveCurrencies.adapter =
            CurrencySelectedAdapter(haveItems) { currencyId ->
                haveItems.removeAll { it.id == currencyId }
                viewModel.haveCurrencies.remove(currencyId)
            }
        binding.haveCurrencies.addItemDecoration(requireActivity().generateFlexboxDecorator())
    }

    private fun requestResults() {
        if (viewModel.wantCurrencies.isEmpty()) {
            Toast.makeText(
                requireActivity(),
                "You must specify items You want",
                Toast.LENGTH_LONG
            ).show()
            return
        }
        if (viewModel.haveCurrencies.isEmpty()) {
            Toast.makeText(
                requireActivity(),
                "You must specify items You have",
                Toast.LENGTH_LONG
            ).show()
            return
        }
        lifecycleScope.launch {
            val progressDialog = requireActivity().getTransparentProgressDialog()
            try {
                progressDialog.show()
                if (!viewModel.requestResult(settings.league)) {
                    Toast.makeText(requireActivity(), "Nothing found!", Toast.LENGTH_LONG).show()
                    return@launch
                }
                CurrencyExchangeResultFragment
                    .newInstance()
                    .showNow(parentFragmentManager, null)
            } finally {
                progressDialog.dismiss()
            }
        }
    }

    companion object {
        private const val WANT_ITEM_ID_KEY = "WANT_ITEM_ID_KEY"
        private const val HAVE_ITEM_ID_KEY = "HAVE_ITEM_ID_KEY"

        fun newInstance(wantItemId: String?, haveItemId: String?) = FragmentScreen {
            CurrencyExchangeMainFragment().apply {
                arguments = bundleOf(WANT_ITEM_ID_KEY to wantItemId, HAVE_ITEM_ID_KEY to haveItemId)
            }
        }
    }
}