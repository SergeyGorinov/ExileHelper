package com.poetradeapp.activities

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import coil.ImageLoader
import com.poetradeapp.R
import com.poetradeapp.adapters.ItemsSearchFieldAdapter
import com.poetradeapp.fragments.PreloadFragment
import com.poetradeapp.fragments.item.ItemExchangeMainFragment
import com.poetradeapp.fragments.item.ItemsSearchResultFragment
import com.poetradeapp.models.enums.ViewState
import com.poetradeapp.models.ui.SearchableItem
import com.poetradeapp.models.view.ItemsSearchViewModel
import com.poetradeapp.ui.SocketsTemplateLoader
import com.poetradeapp.ui.hideKeyboard
import kotlinx.android.synthetic.main.activity_item_search.*
import kotlinx.android.synthetic.main.fragment_item_exchange_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalCoroutinesApi
class ItemsSearchActivity : FragmentActivity() {

    private val preloadFragment = PreloadFragment()
    private val itemExchangeMainFragment = ItemExchangeMainFragment()
    private val itemExchangeResultFragment = ItemsSearchResultFragment()

    private val viewModel: ItemsSearchViewModel by viewModel()

    val imageLoader: ImageLoader by inject()
    val socketsTemplate: SocketsTemplateLoader by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_search)

        items_toolbar.setOnClickListener {
            showToolbarSearchLayout()
        }

        items_toolbar.setNavigationOnClickListener {
            println("navigation click")
            //TODO NAVIGATION MENU
        }

        items_toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.accept -> {
                    this.hideKeyboard(items_toolbar)
                    viewModel.loadingState.value = ViewState.ResultsLoading
                    GlobalScope.launch(Dispatchers.Default) {
                        viewModel.fetchPartialResults(0)
                        if (viewModel.fetchedItems.isNotEmpty()) {
                            viewModel.loadingState.value = ViewState.ResultsLoaded
                        } else {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    this@ItemsSearchActivity,
                                    "Items not found!",
                                    Toast.LENGTH_LONG
                                )
                            }
                        }
                    }
                    true
                }
                else ->
                    false
            }
        }

        toolbar_search_close.setOnClickListener {
            hideToolbarSearchLayout()
        }

        supportFragmentManager
            .beginTransaction()
            .add(R.id.itemExchangeContainer, preloadFragment)
            .commit()

        GlobalScope.launch(Dispatchers.Main) {
            viewModel.loadingState.collect { state ->
                when (state) {
                    ViewState.Loading -> {
                        withContext(Dispatchers.IO) {
                            viewModel.initData()
                        }
                        val adapter = ItemsSearchFieldAdapter(
                            this@ItemsSearchActivity,
                            R.layout.dropdown_item,
                            viewModel.items
                        )
                        adapter.setNotifyOnChange(true)
                        toolbar_search_input.setAdapter(adapter)
                        supportFragmentManager
                            .beginTransaction()
                            .add(R.id.itemExchangeContainer, itemExchangeMainFragment)
                            .commit()
                    }
                    ViewState.Loaded -> {
                        supportFragmentManager
                            .beginTransaction()
                            .remove(preloadFragment)
                            .commit()
                        items_toolbar.title =
                            resources.getString(R.string.items_search_title, "None")
                        appbar.visibility = View.VISIBLE
                    }
                    ViewState.ResultsLoading -> {
                        resultsLoading.visibility = View.VISIBLE
                    }
                    ViewState.ResultsLoadingFailed -> {
                        resultsLoading.visibility = View.GONE
                    }
                    ViewState.ResultsLoaded -> {
                        supportFragmentManager
                            .beginTransaction()
                            .add(R.id.itemExchangeContainer, itemExchangeResultFragment)
                            .hide(itemExchangeMainFragment)
                            .commit()
                        items_toolbar.visibility = View.GONE
                        resultsLoading.visibility = View.GONE
                    }
                }
            }
        }

        toolbar_search_input.setOnItemClickListener { adapterView, _, position, _ ->
            val selectedItem = adapterView.getItemAtPosition(position)
            val adapter = adapterView.adapter
            if (selectedItem is SearchableItem) {
                viewModel.setType(selectedItem.type)
                viewModel.setName(selectedItem.name)
                if (adapter is ItemsSearchFieldAdapter)
                    adapter.selectedItem = selectedItem
                this.hideKeyboard(toolbar_search_input)
                hideToolbarSearchLayout()
                items_toolbar.title =
                    resources.getString(R.string.items_search_title, selectedItem.text)
            }
        }

        toolbar_search_input.setOnFocusChangeListener { view, focused ->
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
                items_toolbar.visibility = View.VISIBLE
            }
            toolbar_search_layout.visibility == View.VISIBLE -> {
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
        items_toolbar.visibility = View.GONE
        toolbar_search_layout.alpha = 0f
        toolbar_search_layout.visibility = View.VISIBLE
        toolbar_search_input.requestFocus()
        toolbar_search_layout
            .animate()
            .alpha(1f)
            .setDuration(250L)
            .withEndAction {
                toolbar_search_input.showDropDown()
            }
            .start()
//        items_toolbar
//            .animate()
//            .alpha(0f)
//            .setDuration(250L)
//            .withEndAction {
//                items_toolbar.visibility = View.GONE
//                toolbar_search_layout.alpha = 0f
//                toolbar_search_layout.visibility = View.VISIBLE
//                toolbar_search_layout
//                    .animate()
//                    .alpha(1f)
//                    .setDuration(250L)
//                    .start()
//            }
//            .start()
    }

    private fun hideToolbarSearchLayout() {
        toolbar_search_layout.visibility = View.GONE
        items_toolbar.alpha = 0f
        items_toolbar.visibility = View.VISIBLE
        items_toolbar
            .animate()
            .alpha(1f)
            .setDuration(250L)
            .start()
//        toolbar_search_layout
//            .animate()
//            .alpha(0f)
//            .setDuration(250L)
//            .withEndAction {
//                toolbar_search_layout.visibility = View.GONE
//                items_toolbar.alpha = 0f
//                items_toolbar.visibility = View.VISIBLE
//                items_toolbar
//                    .animate()
//                    .alpha(1f)
//                    .setDuration(250L)
//                    .start()
//            }
//            .start()
    }
}