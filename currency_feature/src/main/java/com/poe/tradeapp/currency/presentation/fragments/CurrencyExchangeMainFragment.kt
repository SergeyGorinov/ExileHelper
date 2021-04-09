package com.poe.tradeapp.currency.presentation.fragments

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
import com.poe.tradeapp.currency.databinding.FragmentCurrencyExchangeMainBinding
import com.poe.tradeapp.currency.presentation.CurrencyExchangeViewModel
import com.poe.tradeapp.currency.presentation.SwipeToDeleteCallback
import com.poe.tradeapp.currency.presentation.TabLayoutListener
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

    private var searchJob: Job? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        getMainActivity()?.showBottomNavBarIfNeeded()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(TAB_POSITION_KEY, binding.tabLayout.selectedTabPosition)
        super.onSaveInstanceState(outState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding = FragmentCurrencyExchangeMainBinding.bind(view)
        binding = getBinding()

        setupToolbar()
        setupCurrencyList()
        setupTabLayout(savedInstanceState?.getInt(TAB_POSITION_KEY) ?: 0)
        setupOnClickListeners()

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

        lifecycleScope.launchWhenResumed {
            viewModel.viewLoadingState.collect {
                if (it) {
                    binding.toolbarProgressBar.show()
                } else {
                    binding.toolbarProgressBar.hide()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.currencies.adapter = null
        scope.close()
    }

    private fun setupToolbar() {
        binding.toolbar.inflateMenu(R.menu.menu_currency)
        binding.toolbar.title = "Currency Exchange"
        binding.toolbar.setOnMenuItemClickListener { item ->
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
            requireActivity().generateCustomDividerDecoration(R.drawable.currency_list_divider)
        )
        itemSwipeHelper.attachToRecyclerView(binding.currencies)
    }

    private fun setupTabLayout(selectedTabPosition: Int) {
        changeTabsFont(ResourcesCompat.getFont(requireActivity(), R.font.fontinsmallcaps))
        if (binding.tabLayout.selectedTabPosition == selectedTabPosition) {
            updateCurrencyList(
                if (selectedTabPosition == 0) viewModel.wantCurrencies else viewModel.haveCurrencies,
                selectedTabPosition == 0
            )
        } else {
            binding.tabLayout.getTabAt(selectedTabPosition)?.let {
                binding.tabLayout.selectTab(it, true)
            }
        }

        binding.tabLayout.addOnTabSelectedListener(
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
    }

    private fun setupOnClickListeners() {
        binding.add.setOnClickListener {
            router.navigateTo(
                CurrencyExchangeGroupsFragment.newInstance(
                    binding.tabLayout.selectedTabPosition == 0
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

    private fun changeTabsFont(font: Typeface?) {
        font ?: return
        val vg = binding.tabLayout.getChildAt(0) as ViewGroup
        val tabsCount = vg.childCount
        for (j in 0 until tabsCount) {
            val vgTab = vg.getChildAt(j) as ViewGroup
            val tabChildCount = vgTab.childCount
            for (i in 0 until tabChildCount) {
                val tabViewChild = vgTab.getChildAt(i)
                if (tabViewChild is TextView) {
                    tabViewChild.typeface = font
                }
            }
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

        fun newInstance(wantItemId: String? = null, haveItemId: String? = null) = FragmentScreen {
            CurrencyExchangeMainFragment().apply {
                arguments = bundleOf(WANT_ITEM_ID_KEY to wantItemId, HAVE_ITEM_ID_KEY to haveItemId)
            }
        }
    }
}