package com.sgorinov.exilehelper.currency.presentation.fragments

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.sgorinov.exilehelper.core.presentation.*
import com.sgorinov.exilehelper.currency.R
import com.sgorinov.exilehelper.currency.databinding.FragmentCurrencyExchangeGroupBinding
import com.sgorinov.exilehelper.currency.presentation.CurrencyExchangeViewModel
import com.sgorinov.exilehelper.currency.presentation.adapters.CurrencyGroupAdapter

internal class CurrencyExchangeGroupFragment :
    BaseFragment(R.layout.fragment_currency_exchange_group) {

    private val viewModel by scopedViewModel<CurrencyExchangeViewModel>(
        FragmentScopes.CURRENCY_FEATURE.scopeId,
        FragmentScopes.CURRENCY_FEATURE
    )

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
        binding.toolbarLayout.toolbar.setNavigationOnClickListener {
            showMenu()
        }

        val alphaAnimator = ObjectAnimator.ofInt(255, 0)
        alphaAnimator.addUpdateListener {
            val value = it.animatedValue as Int
            if (value >= 0) {
                binding.accept.imageAlpha = value
            }
        }

        val rotateAnimator = ObjectAnimator.ofFloat(0f, -90f)
        rotateAnimator.addUpdateListener {
            val value = it.animatedValue as Float
            if (value >= -90f) {
                binding.accept.rotation = value
            }
        }

        val alphaReverseAnimator = ObjectAnimator.ofInt(0, 255)
        alphaReverseAnimator.addUpdateListener {
            val value = it.animatedValue as Int
            if (value <= 255) {
                binding.accept.imageAlpha = value
            }
        }

        val rotateReverseAnimator = ObjectAnimator.ofFloat(-90f, 0f)
        rotateReverseAnimator.addUpdateListener {
            val value = it.animatedValue as Float
            if (value <= 0f) {
                binding.accept.rotation = value
            }
        }

        binding.accept.setOnClickListener {
            router.backTo(null)
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