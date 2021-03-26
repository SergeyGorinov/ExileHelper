package com.poe.tradeapp.exchange.presentation.fragments

import android.os.Bundle
import android.view.View
import android.widget.AutoCompleteTextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.poe.tradeapp.core.presentation.BaseFragment
import com.poe.tradeapp.core.presentation.getTransparentProgressDialog
import com.poe.tradeapp.core.presentation.hideKeyboard
import com.poe.tradeapp.exchange.R
import com.poe.tradeapp.exchange.databinding.FragmentItemExchangeMainBinding
import com.poe.tradeapp.exchange.presentation.ItemsSearchViewModel
import com.poe.tradeapp.exchange.presentation.adapters.ItemsFiltersListAdapter
import com.poe.tradeapp.exchange.presentation.adapters.ItemsSearchFieldAdapter
import com.poe.tradeapp.exchange.presentation.models.SearchableItem
import com.poe.tradeapp.exchange.presentation.models.enums.ViewFilters
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ItemsSearchMainFragment : BaseFragment(R.layout.fragment_item_exchange_main) {

    private val viewModel by sharedViewModel<ItemsSearchViewModel>()

    private lateinit var binding: FragmentItemExchangeMainBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding = FragmentItemExchangeMainBinding.bind(view)
        binding = getBinding()

        val progressBar = requireActivity().getTransparentProgressDialog()

        binding.itemsToolbar.title =
            resources.getString(R.string.items_search_title, "None")
        binding.appbar.visibility = View.VISIBLE
        binding.itemsToolbar.setOnClickListener {
            showToolbarSearchLayout()
        }

        binding.itemsToolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.accept -> {
                    requireActivity().hideKeyboard(binding.itemsToolbar)
                    lifecycleScope.launch {
                        viewModel.fetchPartialResults(0)
//                        if (viewModel.responseItems.value.isNotEmpty()) {
//                            viewModel.loadingState.value = ViewState.ResultsLoaded
//                        } else {
//                            Toast.makeText(
//                                this@ItemsSearchActivity,
//                                "Items not found!",
//                                Toast.LENGTH_LONG
//                            ).show()
//                        }
                    }
                    true
                }
                else ->
                    false
            }
        }

        binding.toolbarSearchClose.setOnClickListener {
            hideToolbarSearchLayout()
        }

        binding.toolbarSearchInput.setOnItemClickListener { adapterView, _, position, _ ->
            val selectedItem = adapterView.getItemAtPosition(position)
            val adapter = adapterView.adapter
            if (selectedItem is SearchableItem) {
                viewModel.setType(selectedItem.type)
                viewModel.setName(selectedItem.name)
                if (adapter is ItemsSearchFieldAdapter)
                    adapter.selectedItem = selectedItem
                requireActivity().hideKeyboard(binding.toolbarSearchInput)
                hideToolbarSearchLayout()
                binding.itemsToolbar.title =
                    resources.getString(R.string.items_search_title, selectedItem.text)
            }
        }

        binding.toolbarSearchInput.setOnFocusChangeListener { view, focused ->
            val adapter = (view as AutoCompleteTextView).adapter
            if (focused && adapter is ItemsSearchFieldAdapter) {
                view.hint = adapter.selectedItem?.text ?: "Search item"
                view.setText("", false)
            }
        }

        val divider = DividerItemDecoration(view.context, RecyclerView.VERTICAL)
        ContextCompat.getDrawable(view.context, R.drawable.table_column_divider)
            ?.let { divider.setDrawable(it) }

        binding.filtersList.setHasFixedSize(true)
        binding.filtersList.layoutManager = LinearLayoutManager(view.context)
        binding.filtersList.adapter = ItemsFiltersListAdapter(
            ViewFilters.AllFilters.values(),
            viewModel.filters
        ).apply {
            setHasStableIds(true)
        }
        binding.filtersList.addItemDecoration(divider)

        lifecycleScope.launchWhenCreated {
            viewModel.viewLoadingState.collect {
                if (it) {
                    progressBar.show()
                } else {
                    progressBar.dismiss()
                }
            }
        }

        lifecycleScope.launchWhenResumed {
            viewModel.requestItems()
            binding.toolbarSearchInput.setAdapter(
                ItemsSearchFieldAdapter(
                    requireActivity(),
                    R.layout.dropdown_item,
                    viewModel.itemGroups
                )
            )
        }
    }

//    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
//        val focusedView = currentFocus
//        if (focusedView is AutoCompleteTextView && ev?.action == MotionEvent.ACTION_UP) {
//            val outRect = Rect()
//            focusedView.getGlobalVisibleRect(outRect)
//            if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
//                focusedView.clearFocus()
//                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//                imm.hideSoftInputFromWindow(focusedView.windowToken, 0)
//            }
//        }
//        return super.dispatchTouchEvent(ev)
//    }

//    override fun onBackPressed() {
//        if (viewBinding?.toolbarSearchLayout?.visibility == View.VISIBLE) {
//            hideToolbarSearchLayout()
//        }
//        else {
//            super.onBackPressed()
//        }
//    }

    private fun showToolbarSearchLayout() {
        binding.itemsToolbar.visibility = View.GONE
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
        binding.toolbarSearchLayout.visibility = View.GONE
        binding.itemsToolbar.alpha = 0f
        binding.itemsToolbar.visibility = View.VISIBLE
        binding.itemsToolbar
            .animate()
            .alpha(1f)
            .setDuration(250L)
            .start()
    }

    companion object {
        fun newInstance() = FragmentScreen { ItemsSearchMainFragment() }
    }
}