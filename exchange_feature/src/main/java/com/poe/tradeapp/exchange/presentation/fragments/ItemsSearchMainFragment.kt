package com.poe.tradeapp.exchange.presentation.fragments

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.poe.tradeapp.core.presentation.*
import com.poe.tradeapp.exchange.R
import com.poe.tradeapp.exchange.databinding.FragmentItemsSearchMainBinding
import com.poe.tradeapp.exchange.presentation.ItemsSearchViewModel
import com.poe.tradeapp.exchange.presentation.adapters.ItemsFiltersListAdapter
import com.poe.tradeapp.exchange.presentation.adapters.ItemsSearchFieldAdapter
import com.poe.tradeapp.exchange.presentation.models.SuggestionItem
import com.poe.tradeapp.exchange.presentation.models.enums.ViewFilters
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding = FragmentItemsSearchMainBinding.bind(view)
        binding = getBinding()

        setupToolbar()
        setupFiltersList()

        binding.selectedItem.text = resources.getString(R.string.items_search_title, "None")

        binding.selectedItemRemove.setOnClickListener {
            viewModel.setItemData(null, null)
            binding.selectedItem.text = resources.getString(R.string.items_search_title, "None")
        }

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

        lifecycleScope.launchWhenCreated {
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
        scope.close()
    }

//    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
//        val anim: Animation = AnimationUtils.loadAnimation(activity, nextAnim)
//        anim.setAnimationListener(AnimationListener(enter) {
//            Handler(requireActivity().mainLooper).postDelayed({
//                setupFiltersList()
//            }, 50L)
//        })
//        return anim
//    }

    fun closeToolbarSearchLayoutIfNeeded(): Boolean {
        return if (binding.toolbarSearchLayout.visibility == View.VISIBLE) {
            hideToolbarSearchLayout()
            true
        } else {
            false
        }
    }

    private fun setupToolbar() {
        binding.toolbar.title = "Items search"
        binding.appbar.visibility = View.VISIBLE

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
                R.id.search -> {
                    showToolbarSearchLayout()
                    true
                }
                else ->
                    false
            }
        }

        binding.toolbarSearchClose.setOnClickListener {
            hideToolbarSearchLayout()
        }

        binding.toolbarSearchInput.typeface =
            ResourcesCompat.getFont(requireActivity(), R.font.fontinsmallcaps)

        binding.toolbarSearchInput.setOnItemClickListener { adapterView, _, position, _ ->
            val selectedItem = adapterView.getItemAtPosition(position)
            val adapter = adapterView.adapter
            if (selectedItem is SuggestionItem) {
                viewModel.setItemData(selectedItem.type, selectedItem.name)
                if (adapter is ItemsSearchFieldAdapter) {
                    adapter.selectedItem = selectedItem
                }
                requireActivity().hideKeyboard(binding.toolbarSearchInput)
                hideToolbarSearchLayout()
                binding.selectedItem.text =
                    resources.getString(R.string.items_search_title, selectedItem.text)
            }
        }

        binding.toolbarSearchInput.setOnFocusChangeListener { focusedView, focused ->
            val adapter = (focusedView as AutoCompleteTextView).adapter
            if (focused && adapter is ItemsSearchFieldAdapter) {
                focusedView.hint = adapter.selectedItem?.text ?: "Search item"
                focusedView.setText("", false)
            }
        }
    }

    private fun setupFiltersList() {
        val divider = DividerItemDecoration(requireActivity(), RecyclerView.VERTICAL)
        ContextCompat.getDrawable(requireActivity(), R.drawable.table_column_divider)
            ?.let { divider.setDrawable(it) }

        binding.filtersList.setHasFixedSize(true)
        binding.filtersList.layoutManager = LinearLayoutManager(requireActivity())
        binding.filtersList.adapter = ItemsFiltersListAdapter(
            ViewFilters.AllFilters.values(),
            viewModel.filters
        ).apply {
            setHasStableIds(true)
        }
        binding.filtersList.addItemDecoration(divider)
    }

    private fun showToolbarSearchLayout() {
        if (binding.toolbarSearchInput.adapter == null) {
            binding.toolbarSearchInput.setAdapter(
                ItemsSearchFieldAdapter(
                    requireActivity(),
                    R.layout.dropdown_item,
                    viewModel.itemGroups
                )
            )
        }
        binding.toolbar.visibility = View.GONE
        binding.toolbarSearchLayout.alpha = 0f
        binding.toolbarSearchLayout.visibility = View.VISIBLE
        binding.toolbarSearchInput.requestFocus()
        binding.toolbarSearchLayout
            .animate()
            .alpha(1f)
            .setDuration(250L)
            .withEndAction {
                binding.toolbarSearchInput.showDropDown()
            }
            .start()
    }

    private fun hideToolbarSearchLayout() {
        requireActivity().hideKeyboard(binding.toolbarSearchLayout)
        binding.toolbarSearchLayout.visibility = View.GONE
        binding.toolbar.alpha = 0f
        binding.toolbar.visibility = View.VISIBLE
        binding.toolbar
            .animate()
            .alpha(1f)
            .setDuration(250L)
            .start()
    }

    private fun requestResult() {
        requireActivity().hideKeyboard(binding.toolbar)
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
        private val enter: Boolean,
        private val onEndAction: () -> Unit
    ) : Animation.AnimationListener {

        override fun onAnimationStart(animation: Animation) = Unit

        override fun onAnimationRepeat(animation: Animation) = Unit

        override fun onAnimationEnd(animation: Animation) {
            if (enter) {
                onEndAction()
            }
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