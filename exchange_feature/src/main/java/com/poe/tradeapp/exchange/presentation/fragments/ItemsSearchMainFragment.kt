package com.poe.tradeapp.exchange.presentation.fragments

import android.os.Bundle
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.poe.tradeapp.core.presentation.BaseFragment
import com.poe.tradeapp.core.presentation.getTransparentProgressDialog
import com.poe.tradeapp.core.presentation.hideKeyboard
import com.poe.tradeapp.exchange.R
import com.poe.tradeapp.exchange.databinding.FragmentItemsSearchMainBinding
import com.poe.tradeapp.exchange.presentation.ItemsSearchViewModel
import com.poe.tradeapp.exchange.presentation.adapters.ItemsFiltersListAdapter
import com.poe.tradeapp.exchange.presentation.adapters.ItemsSearchFieldAdapter
import com.poe.tradeapp.exchange.presentation.models.SuggestionItem
import com.poe.tradeapp.exchange.presentation.models.enums.ViewFilters
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ItemsSearchMainFragment : BaseFragment(R.layout.fragment_items_search_main) {

    private val viewModel by sharedViewModel<ItemsSearchViewModel>()

    private lateinit var binding: FragmentItemsSearchMainBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding = FragmentItemsSearchMainBinding.bind(view)
        binding = getBinding()

        val progressBar = requireActivity().getTransparentProgressDialog()

        binding.toolbar.title =
            resources.getString(R.string.items_search_title, "None")
        binding.appbar.visibility = View.VISIBLE
        binding.toolbar.setOnClickListener {
            showToolbarSearchLayout()
        }

        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.accept -> {
                    requireActivity().hideKeyboard(binding.toolbar)
                    lifecycleScope.launch {
                        viewModel.viewLoadingState.emit(true)
                        viewModel.fetchPartialResults(settings.league, 0)
                        viewModel.viewLoadingState.emit(false)
                        if (viewModel.itemsResultData?.second?.isNotEmpty() == true) {
                            ItemsSearchResultFragment.newInstance().show(
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
                viewModel.type = selectedItem.type
                viewModel.name = selectedItem.name
                if (adapter is ItemsSearchFieldAdapter)
                    adapter.selectedItem = selectedItem
                requireActivity().hideKeyboard(binding.toolbarSearchInput)
                hideToolbarSearchLayout()
                binding.toolbar.title =
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
        binding.toolbarSearchLayout.visibility = View.GONE
        binding.toolbar.alpha = 0f
        binding.toolbar.visibility = View.VISIBLE
        binding.toolbar
            .animate()
            .alpha(1f)
            .setDuration(250L)
            .start()
    }

    companion object {
        fun newInstance() = FragmentScreen { ItemsSearchMainFragment() }
    }
}