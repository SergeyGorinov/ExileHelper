package com.poe.tradeapp.exchange.presentation

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.poe.tradeapp.core.DI
import com.poe.tradeapp.core.presentation.hideKeyboard
import com.poe.tradeapp.exchange.R
import com.poe.tradeapp.exchange.databinding.ActivityItemSearchBinding
import com.poe.tradeapp.exchange.domain.ItemsSearchViewModel
import com.poe.tradeapp.exchange.presentation.adapters.ItemsSearchFieldAdapter
import com.poe.tradeapp.exchange.presentation.fragments.ItemsSearchMainFragment
import com.poe.tradeapp.exchange.presentation.fragments.ItemsSearchResultFragment
import com.poe.tradeapp.exchange.presentation.models.SearchableItem
import com.poe.tradeapp.exchange.presentation.models.enums.ViewState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.core.component.inject

@ExperimentalCoroutinesApi
class ItemsSearchActivity : FragmentActivity() {

    val socketsTemplate by DI.inject<SocketsTemplateLoader>()

    private val viewModel by viewModels<ItemsSearchViewModel>()

    private val itemExchangeMainFragment =
        ItemsSearchMainFragment()
    private val itemExchangeResultFragment =
        ItemsSearchResultFragment()

    private var viewBinding: ActivityItemSearchBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityItemSearchBinding.inflate(layoutInflater)
        val binding = viewBinding as ActivityItemSearchBinding

        setContentView(binding.root)

        binding.itemsToolbar.setOnClickListener {
            showToolbarSearchLayout()
        }

        binding.itemsToolbar.setNavigationOnClickListener {
            println("navigation click")
            //TODO NAVIGATION MENU
        }

        binding.itemsToolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.accept -> {
                    this.hideKeyboard(binding.itemsToolbar)
                    viewModel.loadingState.value = ViewState.ResultsLoading
                    lifecycleScope.launch {
                        viewModel.fetchPartialResults(0)
                        if (viewModel.responseItems.value.isNotEmpty()) {
                            viewModel.loadingState.value = ViewState.ResultsLoaded
                        } else {
                            Toast.makeText(
                                this@ItemsSearchActivity,
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

//        supportFragmentManager
//            .beginTransaction()
//            .add(R.id.itemExchangeContainer, preloadFragment)
//            .commit()

        viewModel.loadingState.onEach { state ->
            when (state) {
                ViewState.Loading -> {
//                    withContext(Dispatchers.IO) {
//                        viewModel.initData()
//                    }
//                    val adapter = ItemsSearchFieldAdapter(
//                        this@ItemsSearchActivity,
//                        R.layout.dropdown_item,
//                        viewModel.items
//                    )
//                    adapter.setNotifyOnChange(true)
//                    binding.toolbarSearchInput.setAdapter(adapter)
                    supportFragmentManager
                        .beginTransaction()
                        .add(R.id.itemExchangeContainer, itemExchangeMainFragment)
                        .commit()
                }
                ViewState.Loaded -> {
//                    supportFragmentManager
//                        .beginTransaction()
//                        .remove(preloadFragment)
//                        .commit()
                    binding.itemsToolbar.title =
                        resources.getString(R.string.items_search_title, "None")
                    binding.appbar.visibility = View.VISIBLE
                }
                ViewState.ResultsLoading -> {
                    itemExchangeMainFragment.showResultsLoader()
                }
                ViewState.ResultsLoadingFailed -> {
                    itemExchangeMainFragment.closeResultsLoader()
                }
                ViewState.ResultsLoaded -> {
                    supportFragmentManager
                        .beginTransaction()
                        .add(R.id.itemExchangeContainer, itemExchangeResultFragment)
                        .hide(itemExchangeMainFragment)
                        .commit()
                    binding.itemsToolbar.visibility = View.GONE
                    itemExchangeMainFragment.closeResultsLoader()
                }
            }
        }.launchIn(lifecycleScope)

        binding.toolbarSearchInput.setOnItemClickListener { adapterView, _, position, _ ->
            val selectedItem = adapterView.getItemAtPosition(position)
            val adapter = adapterView.adapter
            if (selectedItem is SearchableItem) {
                viewModel.setType(selectedItem.type)
                viewModel.setName(selectedItem.name)
                if (adapter is ItemsSearchFieldAdapter)
                    adapter.selectedItem = selectedItem
                this.hideKeyboard(binding.toolbarSearchInput)
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
    }

    override fun onBackPressed() {
        when {
            itemExchangeResultFragment.isVisible -> {
                supportFragmentManager
                    .beginTransaction()
                    .remove(itemExchangeResultFragment)
                    .show(itemExchangeMainFragment)
                    .commit()
                viewBinding?.itemsToolbar?.visibility = View.VISIBLE
            }
            viewBinding?.toolbarSearchLayout?.visibility == View.VISIBLE -> {
                hideToolbarSearchLayout()
            }
            else ->
                super.onBackPressed()
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val focusedView = currentFocus
        if (focusedView is AutoCompleteTextView && ev?.action == MotionEvent.ACTION_UP) {
            val outRect = Rect()
            focusedView.getGlobalVisibleRect(outRect)
            if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                focusedView.clearFocus()
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(focusedView.windowToken, 0)
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun showToolbarSearchLayout() {
        viewBinding?.itemsToolbar?.visibility = View.GONE
        viewBinding?.toolbarSearchLayout?.alpha = 0f
        viewBinding?.toolbarSearchLayout?.visibility = View.VISIBLE
        viewBinding?.toolbarSearchInput?.requestFocus()
        viewBinding?.toolbarSearchLayout
            ?.animate()
            ?.alpha(1f)
            ?.setDuration(250L)
            ?.withEndAction {
                viewBinding?.toolbarSearchInput?.showDropDown()
            }
            ?.start()
    }

    private fun hideToolbarSearchLayout() {
        viewBinding?.toolbarSearchLayout?.visibility = View.GONE
        viewBinding?.itemsToolbar?.alpha = 0f
        viewBinding?.itemsToolbar?.visibility = View.VISIBLE
        viewBinding?.itemsToolbar
            ?.animate()
            ?.alpha(1f)
            ?.setDuration(250L)
            ?.start()
    }
}