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
import com.sgorinov.exilehelper.exchange.databinding.FragmentItemsSearchMainBinding
import com.sgorinov.exilehelper.exchange.databinding.ItemsSearchFeatureToolbarBinding
import com.sgorinov.exilehelper.exchange.presentation.ItemsSearchViewModel
import com.sgorinov.exilehelper.exchange.presentation.adapters.ItemsFiltersListAdapter
import com.sgorinov.exilehelper.exchange.presentation.adapters.ItemsSearchFieldAdapter
import com.sgorinov.exilehelper.exchange.presentation.models.SuggestionItem
import com.sgorinov.exilehelper.exchange.presentation.models.enums.ViewFilters
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ItemsSearchMainFragment : BaseFragment(R.layout.fragment_items_search_main) {

    private val scope by fragmentLifecycleScope(
        FragmentScopes.EXCHANGE_FEATURE.scopeId,
        FragmentScopes.EXCHANGE_FEATURE
    )

    private val viewModel by scopedViewModel<ItemsSearchViewModel>(
        FragmentScopes.EXCHANGE_FEATURE.scopeId,
        FragmentScopes.EXCHANGE_FEATURE
    )

    private val itemType by lazy { requireArguments().getString(ITEM_TYPE_KEY) }
    private val itemName by lazy { requireArguments().getString(ITEM_NAME_KEY) }
    private val withNotificationRequest by lazy {
        requireArguments().getBoolean(
            WITH_NOTIFICATION_REQUEST_KEY, false
        )
    }

    private lateinit var binding: FragmentItemsSearchMainBinding
    private lateinit var toolbarLayout: ItemsSearchFeatureToolbarBinding
    private lateinit var adapter: ItemsFiltersListAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
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

        if (withNotificationRequest) {
            NotificationRequestAddFragment
                .newInstance(itemType, itemName)
                .show(parentFragmentManager, null)
        } else {
            itemType?.let {
                viewModel.setItemData(it, itemName)
                requestResult()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.close()
    }

    fun closeToolbarSearchLayoutIfNeeded(): Boolean {
        return if (toolbarLayout.toolbarSearchLayout.visibility == View.VISIBLE) {
            hideToolbarSearchLayout()
            true
        } else {
            false
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
                        val items = viewModel.getNotificationRequests()
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

        toolbarLayout.selectedItem.text = resources.getString(R.string.items_search_title, "None")

        toolbarLayout.selectedItemRemove.setOnClickListener {
            (toolbarLayout.toolbarSearchInput.adapter as? ItemsSearchFieldAdapter)?.selectedItem =
                null
        }
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
                        adapter.setupData(ViewFilters.AllFilters.values())
                    }
                )
            }
        } else {
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

        private const val ITEM_TYPE_KEY = "ITEM_TYPE_KEY"
        private const val ITEM_NAME_KEY = "ITEM_NAME_KEY"
        private const val WITH_NOTIFICATION_REQUEST_KEY = "WITH_NOTIFICATION_REQUEST_KEY"

        fun newInstance(
            itemType: String? = null,
            itemName: String? = null,
            withNotificationRequest: Boolean = false
        ): FragmentScreen {
            return FragmentScreen {
                ItemsSearchMainFragment().apply {
                    arguments = bundleOf(
                        ITEM_TYPE_KEY to itemType,
                        ITEM_NAME_KEY to itemName,
                        WITH_NOTIFICATION_REQUEST_KEY to withNotificationRequest
                    )
                }
            }
        }
    }
}