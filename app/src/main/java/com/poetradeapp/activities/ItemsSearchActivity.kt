package com.poetradeapp.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import coil.ImageLoader
import com.poetradeapp.R
import com.poetradeapp.adapters.ItemsSearchFieldAdapter
import com.poetradeapp.fragments.PreloadFragment
import com.poetradeapp.fragments.item.ItemExchangeMainFragment
import com.poetradeapp.fragments.item.ItemsSearchResultFragment
import com.poetradeapp.http.RequestService
import com.poetradeapp.models.responsemodels.ExchangeItemsResponseModel
import com.poetradeapp.models.viewmodels.ItemsSearchViewModel
import com.poetradeapp.ui.SocketsTemplateLoader
import kotlinx.android.synthetic.main.activity_item_search.*
import kotlinx.android.synthetic.main.fragment_item_exchange_main.*
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import retrofit2.await

@ExperimentalCoroutinesApi
class ItemsSearchActivity : FragmentActivity() {

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(this.application)
        ).get(ItemsSearchViewModel::class.java)
    }

    private val preloadFragment = PreloadFragment()
    private val itemExchangeMainFragment = ItemExchangeMainFragment()
    private val itemExchangeResultFragment = ItemsSearchResultFragment()

    private val retrofit: RequestService by inject()
    val imageLoader: ImageLoader by inject()
    val socketsTemplate: SocketsTemplateLoader by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_search)

        items_toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.accept -> {
                    resultsLoading.visibility = View.VISIBLE
                    GlobalScope.launch(Dispatchers.Default) {
                        viewModel.setItemsResultData(getItemsExchangeData())
                    }.invokeOnCompletion {
                        GlobalScope.launch(Dispatchers.Main) {
                            supportFragmentManager
                                .beginTransaction()
                                .add(
                                    R.id.itemExchangeContainer,
                                    itemExchangeResultFragment
                                )
                                .hide(itemExchangeMainFragment)
                                .commit()
                        }
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

        toolbar_search_close.setOnClickListener {
            hideToolbarSearchLayout()
        }

        supportFragmentManager
            .beginTransaction()
            .add(R.id.itemExchangeContainer, preloadFragment)
            .commit()

        GlobalScope.launch(Dispatchers.Main) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.itemExchangeContainer, itemExchangeMainFragment)
                .commit()
        }

        toolbar_search_input.addTextChangedListener(object : TextWatcher {

            private val founded = arrayListOf<Triple<Int, String, String?>>()

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (toolbar_search_input.isPerformingCompletion)
                    return
                GlobalScope.launch(Dispatchers.Default) {
                    founded.clear()
//                    staticDataInstance.getItemsData().forEach { items ->
//                        val includedItems = items.entries.filter { item ->
//                            item.text.toLowerCase(Locale.getDefault())
//                                .contains(
//                                    p0.toString().toLowerCase(
//                                        Locale.getDefault()
//                                    )
//                                )
//                        }
//                        if (includedItems.isNotEmpty()) {
//                            founded.add(Triple(0, items.label, null))
//                            includedItems.forEach {
//                                founded.add(Triple(1, it.type, it.name))
//                            }
//                        }
//                    }
                }.invokeOnCompletion {
                    GlobalScope.launch(Dispatchers.Main) {
                        toolbar_search_input.setAdapter(
                            ItemsSearchFieldAdapter(
                                this@ItemsSearchActivity,
                                R.layout.dropdown_item,
                                R.id.itemText,
                                founded
                            )
                        )
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) = Unit
        })

        toolbar_search_input.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, i, _ ->
                val selectedItem =
                    (toolbar_search_input.adapter as ItemsSearchFieldAdapter).getItem(i)
                selectedItem?.let {
                    if (selectedItem.third != null)
                        toolbar_search_input.setText("${selectedItem.third} ${selectedItem.second}")
                    else
                        toolbar_search_input.setText(selectedItem.second)

                    viewModel.getItemRequestData().query.type = selectedItem.second
                    viewModel.getItemRequestData().query.name = selectedItem.third
                }
            }
    }

    override fun onBackPressed() {
        if (itemExchangeResultFragment.isVisible) {
            supportFragmentManager
                .beginTransaction()
                .remove(itemExchangeResultFragment)
                .show(itemExchangeMainFragment)
                .commit()
            return
        }
        if (toolbar_search_layout.visibility == View.VISIBLE) {
            hideToolbarSearchLayout()
            return
        }
        super.onBackPressed()
    }

    fun closeLoader() {
        supportFragmentManager
            .beginTransaction()
            .remove(preloadFragment)
            .commit()
    }

    fun closeResultsLoader() {
        resultsLoading.visibility = View.GONE
    }

    private fun showToolbarSearchLayout() {
        items_toolbar
            .animate()
            .alpha(0f)
            .setDuration(250L)
            .withEndAction {
                items_toolbar.visibility = View.GONE
                toolbar_search_layout.alpha = 0f
                toolbar_search_layout.visibility = View.VISIBLE
                toolbar_search_layout
                    .animate()
                    .alpha(1f)
                    .setDuration(250L)
                    .start()
            }
            .start()
    }

    private fun hideToolbarSearchLayout() {
        toolbar_search_layout
            .animate()
            .alpha(0f)
            .setDuration(250L)
            .withEndAction {
                toolbar_search_layout.visibility = View.GONE
                items_toolbar.alpha = 0f
                items_toolbar.visibility = View.VISIBLE
                items_toolbar
                    .animate()
                    .alpha(1f)
                    .setDuration(250L)
                    .start()
            }
            .start()
    }

    private suspend fun getItemsExchangeData(): List<ExchangeItemsResponseModel> {
        val baseFetchUrl = StringBuilder("/api/trade/fetch/")
        val service = retrofit

        return withContext(Dispatchers.Default) {
            val resultList = service.getItemsExchangeList(
                "api/trade/search/Heist", viewModel.getItemRequestData()
            ).await()

            resultList.result.let {
                if (it.size > 20) {
                    baseFetchUrl.append(it.subList(0, 10).joinToString(separator = ","))
                } else {
                    baseFetchUrl.append(it.joinToString(separator = ","))
                }
                baseFetchUrl.append("?query=${resultList.id}")

                service.getItemExchangeResponse(baseFetchUrl.toString()).await().result
            }
        }
    }
}