package com.sgorinov.exilehelper.exchange.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
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
    private lateinit var adapter: ItemsFiltersListAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        restoreState()
        adapter = ItemsFiltersListAdapter(viewModel.getFilters())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding = FragmentItemsSearchMainBinding.bind(view)
        binding = getBinding()

        toolbarLayout = ItemsSearchFeatureToolbarBinding.bind(binding.root)

        setupToolbar()
        binding.filters.layoutManager = LinearLayoutManager(requireActivity())
        binding.filters.adapter = adapter

        binding.accept.setOnClickListener {
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

    override fun onDestroy() {
        super.onDestroy()
        binding.filters.adapter = null
        scope.close()
        getMainActivity()?.saveItemsSearchFragmentState(savedState)
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        return if (enter) {
            AnimationUtils.loadAnimation(requireActivity(), nextAnim).apply {
                setAnimationListener(
                    AnimationListener(Handler(requireActivity().mainLooper)) {
                        lifecycleScope.launchWhenResumed {
                            viewModel.viewLoadingState.collect {
                                if (it) {
                                    toolbarLayout.toolbarProgressBar.show()
                                } else {
                                    toolbarLayout.toolbarProgressBar.hide()
                                }
                            }
                        }
                        adapter.setupData(ViewFilters.allFilters)
                    }
                )
            }
        } else {
            null
        }
    }

    fun closeToolbarSearchLayoutIfNeeded(): Boolean {
        return if (toolbarLayout.toolbarSearchLayout.visibility == View.VISIBLE) {
            hideToolbarSearchLayout()
            true
        } else {
            false
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
        val withExchangeRequest = savedState.getBoolean(WITH_EXCHANGE_REQUEST_KEY, false)
        val withNotificationRequest = savedState.getBoolean(WITH_NOTIFICATION_REQUEST_KEY, false)
        if (withNotificationRequest) {
            NotificationRequestAddFragment
                .newInstance(savedItemType, savedItemName)
                .show(parentFragmentManager, null)
            return
        }
        if (withExchangeRequest) {
            savedItemType?.let {
                viewModel.setItemData(it, savedItemName)
                requestResult()
            }
            return
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
                            parentFragmentManager,
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
        requireActivity().hideKeyboard(toolbarLayout.toolbar)
        lifecycleScope.launch {
            viewModel.viewLoadingState.emit(true)
            val results = viewModel.fetchPartialResults(settings.league, 0)
            viewModel.viewLoadingState.emit(false)
            if (results.isNotEmpty()) {
                ItemsSearchResultFragment.newInstance(results).show(
                    parentFragmentManager,
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

    private class AnimationListener(
        private val handler: Handler,
        private val onEndAction: () -> Unit
    ) : Animation.AnimationListener {

        override fun onAnimationStart(animation: Animation?) = Unit

        override fun onAnimationRepeat(animation: Animation?) = Unit

        override fun onAnimationEnd(animation: Animation?) {
            // allow animation end completely
            handler.postDelayed({ onEndAction() }, 50L)
        }
    }

    companion object {
        const val NOTIFICATION_REQUESTS_TYPE = "1"
        const val WITH_NOTIFICATION_REQUEST_KEY = "WITH_NOTIFICATION_REQUEST_KEY"
        const val WITH_EXCHANGE_REQUEST_KEY = "WITH_EXCHANGE_REQUEST_KEY"
        const val SAVED_FILTERS = "SAVED_FILTERS"
        const val SAVED_ITEM_NAME = "SAVED_ITEM_NAME"
        const val SAVED_ITEM_TYPE = "SAVED_ITEM_TYPE"

        fun newInstance() = FragmentScreen { ItemsSearchMainFragment() }
    }
}