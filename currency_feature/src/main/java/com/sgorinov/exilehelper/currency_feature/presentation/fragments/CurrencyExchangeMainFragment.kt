package com.sgorinov.exilehelper.currency_feature.presentation.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.sgorinov.exilehelper.core.presentation.*
import com.sgorinov.exilehelper.currency_feature.R
import com.sgorinov.exilehelper.currency_feature.databinding.CurrencyFeatureToolbarBinding
import com.sgorinov.exilehelper.currency_feature.databinding.FragmentCurrencyExchangeMainBinding
import com.sgorinov.exilehelper.currency_feature.presentation.CurrencyExchangeViewModel
import com.sgorinov.exilehelper.currency_feature.presentation.SwipeToDeleteCallback
import com.sgorinov.exilehelper.currency_feature.presentation.adapters.CurrencySelectedAdapter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import retrofit2.HttpException

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
    private lateinit var adapter: CurrencySelectedAdapter
    private lateinit var toolbarLayout: CurrencyFeatureToolbarBinding

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent ?: return
            if (intent.action == NOTIFICATION_ACTION) {
                processExternalAction(
                    false,
                    intent.getStringExtra(WANT_ITEM_ID_KEY),
                    intent.getStringExtra(HAVE_ITEM_ID_KEY)
                )
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getMainActivity()?.showBottomNavBarIfNeeded()
        getMainActivity()?.checkApiConnection()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding = FragmentCurrencyExchangeMainBinding.bind(view)
        binding = getBinding()

        restoreState()

        savedState.getString(SAVED_STOCK_KEY)?.let { stock ->
            binding.minimumStockValue.setText(stock)
        }

        toolbarLayout = CurrencyFeatureToolbarBinding.bind(binding.root)
        binding.fullfilable.isChecked = savedState.getBoolean(SAVED_FULFILLABLE_STATE_KEY, false)

        setupCurrencyList()
        setupOnClickListeners()
        setupToolbar(savedState.getInt(SAVED_TAB_POSITION_KEY, 0))
        savedState.clear()
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(requireActivity()).registerReceiver(
            receiver,
            IntentFilter(NOTIFICATION_ACTION)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        savedState.clear()
        savedState.putAll(
            bundleOf(
                SAVED_WANT_ITEMS_IDS_KEY to ArrayList(viewModel.wantCurrencies),
                SAVED_HAVE_ITEMS_IDS_KEY to ArrayList(viewModel.haveCurrencies),
                SAVED_TAB_POSITION_KEY to toolbarLayout.tabLayout.selectedTabPosition,
                SAVED_FULFILLABLE_STATE_KEY to binding.fullfilable.isChecked,
                SAVED_STOCK_KEY to binding.minimumStockValue.text?.toString()
            )
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.currencies.adapter = null
        scope.close()
        getMainActivity()?.saveCurrencyExchangeFragmentState(savedState)
    }

    fun processExternalAction(isNotificationRequest: Boolean, vararg args: String?) {
        val wantItemId = args.getOrNull(0)
        val haveItemId = args.getOrNull(1)
        if (wantItemId != null && haveItemId != null) {
            if (isNotificationRequest) {
                NotificationRequestAddFragment
                    .newInstance(wantItemId, haveItemId)
                    .show(childFragmentManager, null)
            } else {
                viewModel.wantCurrencies.clear()
                viewModel.haveCurrencies.clear()
                viewModel.wantCurrencies.add(wantItemId)
                viewModel.haveCurrencies.add(haveItemId)
                requestResults()
            }
        }
    }

    private fun restoreState() {
        savedState.clear()
        getMainActivity()?.restoreCurrencyExchangeFragmentState()?.let {
            savedState.putAll(it)
        }
        savedState.let {
            it.getStringArrayList(SAVED_WANT_ITEMS_IDS_KEY)?.let { wantCurrencies ->
                viewModel.wantCurrencies.addAll(wantCurrencies)
            }
            it.getStringArrayList(SAVED_HAVE_ITEMS_IDS_KEY)?.let { haveCurrencies ->
                viewModel.haveCurrencies.addAll(haveCurrencies)
            }
        }
    }

    private fun setupToolbar(selectedTabPosition: Int) {
        toolbarLayout.toolbar.inflateMenu(R.menu.menu_currency)
        toolbarLayout.toolbar.title = "Currency Exchange"
        toolbarLayout.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.notifications -> {
                    lifecycleScope.launch {
                        item.isEnabled = false
                        val items = viewModel.getNotificationRequests(settings.league)
                        NotificationRequestsFragment.newInstance(items).show(
                            childFragmentManager,
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

        toolbarLayout.toolbar.setNavigationOnClickListener {
            showMenu()
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
        lifecycleScope.launch {
            binding.search.isEnabled = false
            val result = try {
                viewModel.requestResult(
                    settings.league,
                    binding.fullfilable.isSelected,
                    binding.minimumStockValue.text.toString(),
                    0
                )
            } catch (e: Exception) {
                val message = if (e is HttpException) {
                    if (e.code() == 404) {
                        "Nothing found"
                    } else {
                        e.response()?.errorBody()?.string()?.run {
                            val errorBody = Json.decodeFromString<JsonObject>(this)
                            val jsonMessage = errorBody["error"]?.jsonObject?.get("message")
                            jsonMessage?.jsonPrimitive?.content ?: "Unknown error"
                        }
                    }
                } else {
                    "Fetching failed"
                }
                AlertDialog.Builder(requireActivity(), R.style.AppTheme_AlertDialog)
                    .setTitle("Error")
                    .setMessage(message)
                    .setPositiveButton("OK") { dialogInterface, _ ->
                        dialogInterface.dismiss()
                    }
                    .show()
                emptyList()
            }
            if (result.isNotEmpty()) {
                CurrencyExchangeResultFragment
                    .newInstance(
                        result,
                        binding.fullfilable.isSelected,
                        binding.minimumStockValue.text.toString()
                    ).show(childFragmentManager, null)
            } else {

            }
            binding.search.isEnabled = true
        }
    }

    companion object {
        const val NOTIFICATION_REQUESTS_TYPE = "0"
        const val NOTIFICATION_ACTION = "CURRENCY_EXCHANGE_NOTIFICATION_ACTION"
        const val WANT_ITEM_ID_KEY = "WANT_ITEM_ID_KEY"
        const val HAVE_ITEM_ID_KEY = "HAVE_ITEM_ID_KEY"
        const val SAVED_TAB_POSITION_KEY = "TAB_POSITION_KEY"
        const val SAVED_FULFILLABLE_STATE_KEY = "FULFILLABLE_STATE_KEY"
        const val SAVED_WANT_ITEMS_IDS_KEY = "WANT_ITEMS_IDS_KEY"
        const val SAVED_HAVE_ITEMS_IDS_KEY = "HAVE_ITEMS_IDS_KEY"
        const val SAVED_STOCK_KEY = "SAVED_STOCK_KEY"

        fun newInstance() = FragmentScreen { CurrencyExchangeMainFragment() }
    }
}