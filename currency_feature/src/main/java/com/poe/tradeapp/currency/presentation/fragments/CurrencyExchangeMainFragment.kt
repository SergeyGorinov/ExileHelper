package com.poe.tradeapp.currency.presentation.fragments

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.poe.tradeapp.core.presentation.*
import com.poe.tradeapp.currency.R
import com.poe.tradeapp.currency.databinding.CurrencyFeatureToolbarBinding
import com.poe.tradeapp.currency.databinding.FragmentCurrencyExchangeMainBinding
import com.poe.tradeapp.currency.presentation.CurrencyExchangeViewModel
import com.poe.tradeapp.currency.presentation.SwipeToDeleteCallback
import com.poe.tradeapp.currency.presentation.adapters.CurrencySelectedAdapter
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CurrencyExchangeMainFragment : BaseFragment(R.layout.fragment_currency_exchange_main) {

    private val scope by fragmentLifecycleScope(
        FragmentScopes.CURRENCY_FEATURE.scopeId,
        FragmentScopes.CURRENCY_FEATURE
    )

    private val viewModel by scopedViewModel<CurrencyExchangeViewModel>(
        FragmentScopes.CURRENCY_FEATURE.scopeId,
        FragmentScopes.CURRENCY_FEATURE
    )

    private val wantItemId by lazy { requireArguments().getString(WANT_ITEM_ID_KEY) }
    private val haveItemId by lazy { requireArguments().getString(HAVE_ITEM_ID_KEY) }

    private lateinit var binding: FragmentCurrencyExchangeMainBinding
    private lateinit var adapter: CurrencySelectedAdapter
    private lateinit var toolbarLayout: CurrencyFeatureToolbarBinding

    private var searchJob: Job? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        getMainActivity()?.showBottomNavBarIfNeeded()
        getMainActivity()?.checkApiConnection()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding = FragmentCurrencyExchangeMainBinding.bind(view)
        binding = getBinding()

        toolbarLayout = CurrencyFeatureToolbarBinding.bind(binding.root)

        setupCurrencyList()
        setupToolbar(savedState.getInt(TAB_POSITION_KEY, 0))
        setupOnClickListeners()

        binding.fullfilable.isChecked = savedState.getBoolean(FULFILLABLE_STATE_KEY, true)

        wantItemId?.let {
            viewModel.wantCurrencies.clear()
            viewModel.wantCurrencies.add(it)
        }
        haveItemId?.let {
            viewModel.haveCurrencies.clear()
            viewModel.haveCurrencies.add(it)
        }
        if (wantItemId != null && haveItemId != null) {
            requestResults()
            requireArguments().clear()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        savedState.putInt(TAB_POSITION_KEY, toolbarLayout.tabLayout.selectedTabPosition)
        savedState.putBoolean(FULFILLABLE_STATE_KEY, binding.fullfilable.isChecked)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.currencies.adapter = null
        scope.close()
    }

    private fun setupToolbar(selectedTabPosition: Int) {
        toolbarLayout.toolbar.inflateMenu(R.menu.menu_currency)
        toolbarLayout.toolbar.title = "Currency Exchange"
        toolbarLayout.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.notifications -> {
                    lifecycleScope.launch {
                        item.isEnabled = false
                        val items = viewModel.getNotificationRequests()
                        NotificationRequestsFragment.newInstance(items).show(
                            parentFragmentManager,
                            null
                        )
                        item.isEnabled = true
                    }
                    true
                }
                else ->
                    false
            }
        }
        toolbarLayout.tabLayout.changeTabsFont(
            ResourcesCompat.getFont(
                requireActivity(),
                R.font.fontinsmallcaps
            )
        )

        updateCurrencyList(
            if (selectedTabPosition == 0) viewModel.wantCurrencies else viewModel.haveCurrencies,
            selectedTabPosition == 0
        )

        if (toolbarLayout.tabLayout.selectedTabPosition != selectedTabPosition) {
            toolbarLayout.tabLayout.getTabAt(selectedTabPosition)?.let {
                toolbarLayout.tabLayout.selectTab(it, true)
            }
        }

        toolbarLayout.tabLayout.addOnTabSelectedListener(
            TabLayoutListener(
                binding.currenciesContainer,
                binding.currencies,
                binding.emptyPlaceholder
            ) {
                updateCurrencyList(
                    if (it == 0) viewModel.wantCurrencies else viewModel.haveCurrencies,
                    it == 0
                )
            }
        )

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

    private fun setupCurrencyList() {
        adapter = CurrencySelectedAdapter { currencyId, isWantList ->
            val selectedItems = if (isWantList) {
                viewModel.wantCurrencies
            } else {
                viewModel.haveCurrencies
            }
            selectedItems.remove(currencyId)
            changeEmptyPlaceholderVisibility(selectedItems.isEmpty())
        }

        val backgroundColor = ColorDrawable(
            ContextCompat.getColor(requireActivity(), R.color.secondaryColor)
        )
        val icon = ContextCompat.getDrawable(requireActivity(), R.drawable.clear_24)
        val itemSwipeHelper = ItemTouchHelper(
            SwipeToDeleteCallback(adapter, backgroundColor, icon!!)
        )
        binding.currencies.layoutManager = LinearLayoutManager(requireActivity())
        binding.currencies.adapter = adapter
        binding.currencies.addItemDecoration(
            requireActivity().generateCustomDividerDecoration(R.drawable.colored_list_divider, 52)
        )
        itemSwipeHelper.attachToRecyclerView(binding.currencies)
    }

    private fun setupOnClickListeners() {
        binding.add.setOnClickListener {
            router.navigateTo(
                CurrencyExchangeGroupsFragment.newInstance(
                    toolbarLayout.tabLayout.selectedTabPosition == 0
                )
            )
        }
        binding.search.setOnClickListener {
            requestResults()
        }
        binding.fullfilable.setOnClickListener {
            it.isSelected = !it.isSelected
        }
    }

    private fun updateCurrencyList(selectedItems: List<String>, isWantList: Boolean) {
        val isEmpty = selectedItems.isEmpty()
        changeEmptyPlaceholderVisibility(isEmpty)
        if (!isEmpty) {
            val items = selectedItems.map { selectedId ->
                viewModel.allCurrencies.flatMap { it.staticItems }.filter { it.id == selectedId }
            }.flatten().toMutableList()
            adapter.setItems(items, isWantList)
        }
    }

    private fun changeEmptyPlaceholderVisibility(isVisible: Boolean) {
        if (isVisible) {
            binding.emptyPlaceholder.visibility = View.VISIBLE
            binding.currencies.visibility = View.GONE
        } else {
            binding.emptyPlaceholder.visibility = View.GONE
            binding.currencies.visibility = View.VISIBLE
        }
    }

    private fun requestResults() {
        if (viewModel.wantCurrencies.isEmpty()) {
            Toast.makeText(
                requireActivity(),
                "Add items You want",
                Toast.LENGTH_LONG
            ).show()
            return
        }
        if (viewModel.haveCurrencies.isEmpty()) {
            Toast.makeText(
                requireActivity(),
                "Add items You have",
                Toast.LENGTH_LONG
            ).show()
            return
        }
        if (searchJob?.isActive == true) {
            searchJob?.cancel()
        }
        searchJob = lifecycleScope.launch {
            binding.search.isEnabled = false
            val result = viewModel.requestResult(
                settings.league,
                binding.fullfilable.isSelected,
                binding.minimumStockValue.text.toString(),
                0
            )
            if (result.isEmpty()) {
                Toast.makeText(requireActivity(), "Nothing found!", Toast.LENGTH_LONG).show()
                binding.search.isEnabled = true
                return@launch
            }
            CurrencyExchangeResultFragment
                .newInstance(
                    result,
                    binding.fullfilable.isSelected,
                    binding.minimumStockValue.text.toString()
                ).show(parentFragmentManager, null)
            binding.search.isEnabled = true
        }
    }

    companion object {
        const val NOTIFICATION_REQUESTS_TYPE = "0"

        private const val WANT_ITEM_ID_KEY = "WANT_ITEM_ID_KEY"
        private const val HAVE_ITEM_ID_KEY = "HAVE_ITEM_ID_KEY"
        private const val TAB_POSITION_KEY = "TAB_POSITION_KEY"
        private const val FULFILLABLE_STATE_KEY = "FULFILLABLE_STATE_KEY"

        fun newInstance(wantItemId: String? = null, haveItemId: String? = null): FragmentScreen {
            return FragmentScreen {
                CurrencyExchangeMainFragment().apply {
                    arguments =
                        bundleOf(WANT_ITEM_ID_KEY to wantItemId, HAVE_ITEM_ID_KEY to haveItemId)
                }
            }
        }
    }
}