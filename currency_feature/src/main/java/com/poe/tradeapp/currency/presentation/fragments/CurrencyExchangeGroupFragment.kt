package com.poe.tradeapp.currency.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.poe.tradeapp.core.presentation.BaseFragment
import com.poe.tradeapp.core.presentation.generateFlexboxDecorator
import com.poe.tradeapp.core.presentation.generateFlexboxManager
import com.poe.tradeapp.currency.R
import com.poe.tradeapp.currency.databinding.FragmentCurrencyExchangeGroupBinding
import com.poe.tradeapp.currency.presentation.CurrencyExchangeViewModel
import com.poe.tradeapp.currency.presentation.adapters.CurrencyGroupAdapter
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

internal class CurrencyExchangeGroupFragment :
    BaseFragment(R.layout.fragment_currency_exchange_group) {

    private val viewModel by sharedViewModel<CurrencyExchangeViewModel>()

    private val groupId by lazy {
        arguments?.getString(GROUP_ID_KEY)
    }
    private val isFromWanted by lazy {
        arguments?.getBoolean(IS_FROM_WANT_KEY, false) ?: false
    }

    private lateinit var binding: FragmentCurrencyExchangeGroupBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentCurrencyExchangeGroupBinding.bind(view)
        binding = getBinding()
        binding.toolbarLayout.toolbar.title = "Select currencies"
        binding.toolbarLayout.toolbar.inflateMenu(R.menu.menu_currency)
        binding.toolbarLayout.toolbar.menu.findItem(R.id.accept).setOnMenuItemClickListener {
            router.backTo(null)
            true
        }
        val group = viewModel.allCurrencies.firstOrNull { it.id == groupId }
        if (group != null) {
            val items = group.staticItems.map {
                if (isFromWanted) {
                    viewModel.wantCurrencies.contains(it.id) to it
                } else {
                    viewModel.haveCurrencies.contains(it.id) to it
                }
            }
            val adapter = CurrencyGroupAdapter(items, group.isTextItems) { isSelected, id ->
                if (isSelected) {
                    if (isFromWanted) {
                        viewModel.wantCurrencies.add(id)
                    } else {
                        viewModel.haveCurrencies.add(id)
                    }
                } else {
                    if (isFromWanted) {
                        viewModel.wantCurrencies.remove(id)
                    } else {
                        viewModel.haveCurrencies.remove(id)
                    }
                }
            }
            binding.currencyGroup.layoutManager = requireActivity().generateFlexboxManager()
            binding.currencyGroup.adapter = adapter
            binding.currencyGroup.addItemDecoration(requireActivity().generateFlexboxDecorator())
        } else {
            router.exit()
        }
    }

    companion object {
        private const val GROUP_ID_KEY = "GROUP_ID_KEY"
        private const val IS_FROM_WANT_KEY = "IS_FROM_WANT_KEY"

        fun newInstance(groupId: String, isFromWant: Boolean) = FragmentScreen {
            CurrencyExchangeGroupFragment().apply {
                arguments =
                    bundleOf(GROUP_ID_KEY to groupId, IS_FROM_WANT_KEY to isFromWant)
            }
        }
    }
}