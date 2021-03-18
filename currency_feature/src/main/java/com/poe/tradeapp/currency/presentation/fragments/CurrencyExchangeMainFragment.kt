package com.poe.tradeapp.currency.presentation.fragments

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.poe.tradeapp.core.presentation.BaseFragment
import com.poe.tradeapp.core.presentation.generateFlexboxDecorator
import com.poe.tradeapp.core.presentation.generateFlexboxManager
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            viewModel.requestItems()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val progressDialog = AlertDialog.Builder(requireActivity()).setView(
            View.inflate(
                requireActivity(),
                R.layout.progress_dialog,
                null
            )
        ).create().apply {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
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

        viewBinding = FragmentCurrencyExchangeMainBinding.bind(view)
        binding = getBinding()

        binding.toolbarLayout.toolbar.inflateMenu(R.menu.menu_currency)
        binding.toolbarLayout.toolbar.title = "Currency Exchange"
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
        restoreState()
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
            val progressDialog = AlertDialog.Builder(requireActivity()).setView(
                View.inflate(
                    requireActivity(),
                    R.layout.progress_dialog,
                    null
                )
            ).create().apply {
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
            progressDialog.show()
            viewModel.requestResult()
            CurrencyExchangeResultFragment
                .newInstance()
                .showNow(parentFragmentManager, null)
            progressDialog.dismiss()
        }
    }

    companion object {
        fun newInstance() = FragmentScreen { CurrencyExchangeMainFragment() }
    }
}