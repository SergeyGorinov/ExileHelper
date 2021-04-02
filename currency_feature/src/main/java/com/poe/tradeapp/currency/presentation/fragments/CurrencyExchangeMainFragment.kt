package com.poe.tradeapp.currency.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.poe.tradeapp.core.presentation.*
import com.poe.tradeapp.currency.R
import com.poe.tradeapp.currency.databinding.FragmentCurrencyExchangeMainBinding
import com.poe.tradeapp.currency.presentation.CurrencyExchangeViewModel
import com.poe.tradeapp.currency.presentation.EnterNotifyValueAlertDialog
import com.poe.tradeapp.currency.presentation.SelectActionAlertDialog
import com.poe.tradeapp.currency.presentation.adapters.CurrencySelectedAdapter
import com.poe.tradeapp.currency.presentation.models.StaticItemViewData
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class CurrencyExchangeMainFragment : BaseFragment(R.layout.fragment_currency_exchange_main) {

    private val scope by fragmentLifecycleScope(
        FragmentScopes.CURRENCY_FEATURE.scopeId,
        FragmentScopes.CURRENCY_FEATURE
    )

    private val viewModel by scopedViewModel<CurrencyExchangeViewModel>(
        FragmentScopes.CURRENCY_FEATURE.scopeId,
        FragmentScopes.CURRENCY_FEATURE
    )

    private lateinit var binding: FragmentCurrencyExchangeMainBinding

    private val wantItemId by lazy { requireArguments().getString(WANT_ITEM_ID_KEY) }
    private val haveItemId by lazy { requireArguments().getString(HAVE_ITEM_ID_KEY) }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        getMainActivity()?.showBottomNavBarIfNeeded()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val progressDialog = requireActivity().getTransparentProgressDialog()

        lifecycleScope.launchWhenResumed {
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
                    SelectActionAlertDialog(
                        requireActivity(),
                        {
                            requestResults()
                        },
                        {
                            val buyingItem = viewModel.allCurrencies.flatMap {
                                it.staticItems
                            }.firstOrNull {
                                viewModel.wantCurrencies.contains(it.id)
                            }
                            val payingItem = viewModel.allCurrencies.flatMap {
                                it.staticItems
                            }.firstOrNull {
                                viewModel.haveCurrencies.contains(it.id)
                            }
                            if (buyingItem != null && payingItem != null) {
                                EnterNotifyValueAlertDialog(
                                    buyingItem.label,
                                    payingItem.label,
                                    requireActivity()
                                ) {
                                    lifecycleScope.launch {
                                        val messagingToken =
                                            suspendCoroutine<String?> { coroutine ->
                                                FirebaseMessaging.getInstance().token.addOnCompleteListener {
                                                    coroutine.resume(it.result)
                                                }
                                            }
                                        val authToken = suspendCoroutine<String?> { coroutine ->
                                            Firebase.auth.currentUser?.getIdToken(false)
                                                ?.addOnCompleteListener {
                                                    coroutine.resume(it.result?.token)
                                                } ?: coroutine.resume(null)
                                        }
                                        setRequest(
                                            buyingItem,
                                            payingItem,
                                            it,
                                            messagingToken,
                                            authToken
                                        )
                                    }
                                }.show()
                            }
                        }
                    ).show()
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

    override fun onDestroy() {
        super.onDestroy()
        scope.close()
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
                val result = viewModel.requestResult(settings.league, 0)
                if (result.isEmpty()) {
                    Toast.makeText(requireActivity(), "Nothing found!", Toast.LENGTH_LONG).show()
                    return@launch
                }
                CurrencyExchangeResultFragment
                    .newInstance(result)
                    .showNow(parentFragmentManager, null)
            } finally {
                progressDialog.dismiss()
            }
        }
    }

    private suspend fun setRequest(
        buyingItem: StaticItemViewData,
        payingItem: StaticItemViewData,
        payingAmount: Int,
        messagingToken: String?,
        authToken: String?
    ) {
        if (messagingToken != null) {
            val toastText = if (viewModel.sendNotificationRequest(
                    messagingToken,
                    buyingItem,
                    payingItem,
                    payingAmount,
                    authToken
                )
            ) {
                "Success!"
            } else {
                "Notification request set failed!"
            }
            Toast.makeText(
                requireActivity(),
                toastText,
                Toast.LENGTH_LONG
            ).show()
        } else {
            Toast.makeText(
                requireActivity(),
                "Messaging token can not be null!",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    companion object {
        private const val WANT_ITEM_ID_KEY = "WANT_ITEM_ID_KEY"
        private const val HAVE_ITEM_ID_KEY = "HAVE_ITEM_ID_KEY"

        fun newInstance(wantItemId: String? = null, haveItemId: String? = null) = FragmentScreen {
            CurrencyExchangeMainFragment().apply {
                arguments = bundleOf(WANT_ITEM_ID_KEY to wantItemId, HAVE_ITEM_ID_KEY to haveItemId)
            }
        }
    }
}