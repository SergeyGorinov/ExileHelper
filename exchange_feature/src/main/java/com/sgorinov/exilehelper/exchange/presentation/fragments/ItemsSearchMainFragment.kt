package com.sgorinov.exilehelper.exchange.presentation.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.sgorinov.exilehelper.core.presentation.*
import com.sgorinov.exilehelper.exchange.R
import com.sgorinov.exilehelper.exchange.data.models.Filter
import com.sgorinov.exilehelper.exchange.databinding.FragmentItemsSearchMainBinding
import com.sgorinov.exilehelper.exchange.databinding.ItemsSearchFeatureToolbarBinding
import com.sgorinov.exilehelper.exchange.presentation.ItemsSearchViewModel
import com.sgorinov.exilehelper.exchange.presentation.adapters.ItemsFiltersListAdapter
import com.sgorinov.exilehelper.exchange.presentation.adapters.ItemsSearchFieldAdapter
import com.sgorinov.exilehelper.exchange.presentation.models.SuggestionItem
import com.sgorinov.exilehelper.exchange.presentation.models.enums.ViewFilters
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

class ItemsSearchMainFragment : BaseFragment(R.layout.fragment_items_search_main) {

    private val scope by fragmentLifecycleScope(
        FragmentScopes.EXCHANGE_FEATURE.scopeId,
        FragmentScopes.EXCHANGE_FEATURE
    )

    private val viewModel by scopedViewModel<ItemsSearchViewModel>(
        FragmentScopes.EXCHANGE_FEATURE.scopeId,
        FragmentScopes.EXCHANGE_FEATURE
    )

    private lateinit var binding: FragmentItemsSearchMainBinding
    private lateinit var toolbarLayout: ItemsSearchFeatureToolbarBinding

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent ?: return
            if (intent.action == NOTIFICATION_ACTION) {
                processExternalAction(
                    false,
                    intent.getStringExtra(SAVED_ITEM_TYPE),
                    intent.getStringExtra(SAVED_ITEM_NAME)
                )
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        getMainActivity()?.showBottomNavBarIfNeeded()
        getMainActivity()?.checkApiConnection()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding = FragmentItemsSearchMainBinding.bind(view)
        binding = getBinding()
        toolbarLayout = ItemsSearchFeatureToolbarBinding.bind(binding.root)

        restoreState()
        setupToolbar()

        binding.accept.setOnClickListener {
            requireActivity().hideKeyboard(binding.root)
            requestResult()
        }
    }

    override fun onDestroyView() {
        val selectedItem =
            (toolbarLayout.toolbarSearchInput.adapter as? ItemsSearchFieldAdapter)?.selectedItem
        savedState.putAll(
            bundleOf(
                SAVED_ITEM_NAME to selectedItem?.name,
                SAVED_ITEM_TYPE to selectedItem?.type,
                SAVED_FILTERS to Json.encodeToString(
                    ListSerializer(Filter.serializer()),
                    viewModel.getFilters()
                )
            )
        )

        super.onDestroyView()
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(requireActivity()).registerReceiver(
            receiver,
            IntentFilter(NOTIFICATION_ACTION)
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
        binding.filters.layoutManager = LinearLayoutManager(requireActivity())
        binding.filters.adapter = ItemsFiltersListAdapter(
            ViewFilters.allFilters,
            viewModel.getFilters()
        ).apply {
            setHasStableIds(true)
        }
        binding.filters.setItemViewCacheSize(10)
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.close()
        getMainActivity()?.saveItemsSearchFragmentState(savedState)
    }

    fun closeToolbarSearchLayoutIfNeeded(): Boolean {
        return if (toolbarLayout.toolbarSearchLayout.visibility == View.VISIBLE) {
            hideToolbarSearchLayout()
            true
        } else {
            false
        }
    }

    fun processExternalAction(isNotificationRequest: Boolean, vararg args: String?) {
        if (isNotificationRequest) {
            NotificationRequestAddFragment
                .newInstance(args.getOrNull(0), args.getOrNull(1))
                .show(childFragmentManager, null)
        } else {
            args.getOrNull(0)?.let {
                viewModel.getFilters().clear()
                viewModel.setItemData(it, args.getOrNull(1))
                requestResult()
            }
        }
    }

    private fun restoreState() {
        savedState.clear()
        getMainActivity()?.restoreItemsSearchFragmentState()?.let {
            savedState.putAll(it)
        }
        val savedItemName = savedState.getString(SAVED_ITEM_NAME)
        val savedItemType = savedState.getString(SAVED_ITEM_TYPE)
        val savedFilters = savedState.getString(SAVED_FILTERS)
        savedItemType?.let {
            viewModel.setItemData(it, savedItemName)
        }
        savedFilters?.run {
            Json.decodeFromString(ListSerializer(Filter.serializer()), this)
        }?.let {
            viewModel.getFilters().addAll(it)
        }
    }

    private fun setupToolbar() {
        toolbarLayout.toolbar.title = "Items search"
        toolbarLayout.appbar.visibility = View.VISIBLE

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
                R.id.search -> {
                    showToolbarSearchLayout()
                    true
                }
                else ->
                    false
            }
        }

        toolbarLayout.toolbarSearchClose.setOnClickListener {
            hideToolbarSearchLayout()
        }

        toolbarLayout.toolbarSearchInput.typeface =
            ResourcesCompat.getFont(requireActivity(), R.font.fontinsmallcaps)

        toolbarLayout.toolbarSearchInput.setOnItemClickListener { adapterView, _, position, _ ->
            val selectedItem = adapterView.getItemAtPosition(position)
            val adapter = adapterView.adapter
            if (selectedItem is SuggestionItem) {
                if (adapter is ItemsSearchFieldAdapter) {
                    adapter.selectedItem = selectedItem
                }
                requireActivity().hideKeyboard(toolbarLayout.toolbarSearchInput)
                hideToolbarSearchLayout()
            }
        }

        toolbarLayout.toolbarSearchInput.setOnFocusChangeListener { focusedView, focused ->
            val adapter = (focusedView as AutoCompleteTextView).adapter
            if (focused && adapter is ItemsSearchFieldAdapter) {
                focusedView.hint = adapter.selectedItem?.text ?: "Search item"
                focusedView.setText("", false)
            }
        }

        val savedItemName = savedState.getString(SAVED_ITEM_NAME)
        val savedItemType = savedState.getString(SAVED_ITEM_TYPE)
        val savedSelectedItem = when {
            savedItemName != null && savedItemType != null -> {
                viewModel.itemGroups.flatMap {
                    it.entries
                }.firstOrNull {
                    it.type == savedItemType && it.name == savedItemName
                }
            }
            savedItemType != null -> {
                viewModel.itemGroups.flatMap {
                    it.entries
                }.firstOrNull {
                    it.type == savedItemType
                }
            }
            else -> null
        }
        val selectedItemText = savedSelectedItem?.let {
            (toolbarLayout.toolbarSearchInput.adapter as? ItemsSearchFieldAdapter)?.selectedItem =
                SuggestionItem(false, it.text, it.type, it.name)
            toolbarLayout.selectedItemRemove.visibility = View.VISIBLE
            it.text
        } ?: "None"

        toolbarLayout.selectedItem.text =
            resources.getString(R.string.items_search_title, selectedItemText)

        toolbarLayout.selectedItemRemove.setOnClickListener {
            (toolbarLayout.toolbarSearchInput.adapter as? ItemsSearchFieldAdapter)?.selectedItem =
                null
        }

        toolbarLayout.toolbar.setNavigationOnClickListener {
            showMenu()
        }
    }

    private fun showToolbarSearchLayout() {
        if (toolbarLayout.toolbarSearchInput.adapter == null) {
            toolbarLayout.toolbarSearchInput.setAdapter(
                ItemsSearchFieldAdapter(
                    requireActivity(),
                    R.layout.dropdown_item,
                    viewModel.itemGroups
                ) { selectedItem ->
                    if (selectedItem != null) {
                        toolbarLayout.selectedItem.text =
                            resources.getString(R.string.items_search_title, selectedItem.text)
                        toolbarLayout.selectedItemRemove.visibility = View.VISIBLE
                    } else {
                        toolbarLayout.selectedItem.text =
                            resources.getString(R.string.items_search_title, "None")
                        toolbarLayout.selectedItemRemove.visibility = View.GONE
                    }
                    viewModel.setItemData(selectedItem?.type, selectedItem?.name)
                }
            )
        }
        toolbarLayout.toolbar.visibility = View.GONE
        toolbarLayout.toolbarSearchLayout.alpha = 0f
        toolbarLayout.toolbarSearchLayout.visibility = View.VISIBLE
        toolbarLayout.toolbarSearchInput.requestFocus()
        toolbarLayout.toolbarSearchLayout
            .animate()
            .alpha(1f)
            .setDuration(250L)
            .withEndAction {
                toolbarLayout.toolbarSearchInput.showDropDown()
            }
            .start()
    }

    private fun hideToolbarSearchLayout() {
        requireActivity().hideKeyboard(toolbarLayout.toolbarSearchLayout)
        toolbarLayout.toolbarSearchLayout.visibility = View.GONE
        toolbarLayout.toolbar.alpha = 0f
        toolbarLayout.toolbar.visibility = View.VISIBLE
        toolbarLayout.toolbar
            .animate()
            .alpha(1f)
            .setDuration(250L)
            .start()
    }

    private fun requestResult() {
        lifecycleScope.launchWhenResumed {
            viewModel.viewLoadingState.emit(true)
            val results = viewModel.fetchPartialResults(settings.league, 0)
            viewModel.viewLoadingState.emit(false)
            if (results.isNotEmpty()) {
                ItemsSearchResultFragment.newInstance(results).show(
                    childFragmentManager,
                    null
                )
            } else {
                Toast.makeText(
                    requireActivity(),
                    "Items not found!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    companion object {
        const val NOTIFICATION_REQUESTS_TYPE = "1"
        const val NOTIFICATION_ACTION = "ITEMS_SEARCH_NOTIFICATION_ACTION"
        const val SAVED_FILTERS = "SAVED_FILTERS"
        const val SAVED_ITEM_NAME = "SAVED_ITEM_NAME"
        const val SAVED_ITEM_TYPE = "SAVED_ITEM_TYPE"

        fun newInstance() = FragmentScreen { ItemsSearchMainFragment() }
    }
}