package com.poe.tradeapp.exchange.presentation.fragments

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.poe.tradeapp.core.presentation.BaseFragment
import com.poe.tradeapp.exchange.R
import com.poe.tradeapp.exchange.databinding.FragmentItemExchangeFiltersBinding
import com.poe.tradeapp.exchange.domain.ItemsSearchViewModel
import com.poe.tradeapp.exchange.presentation.adapters.ItemsFiltersListAdapter
import com.poe.tradeapp.exchange.presentation.models.enums.ViewFilters
import com.poe.tradeapp.exchange.presentation.models.enums.ViewState
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class ItemsSearchFiltersFragment : BaseFragment(R.layout.fragment_item_exchange_filters) {

    private val viewModel by activityViewModels<ItemsSearchViewModel>()

    private lateinit var binding: FragmentItemExchangeFiltersBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding = FragmentItemExchangeFiltersBinding.bind(view)
        binding = getBinding()

        val divider = DividerItemDecoration(view.context, RecyclerView.VERTICAL)
        ContextCompat.getDrawable(view.context, R.drawable.table_column_divider)
            ?.let { divider.setDrawable(it) }

        binding.filtersList.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewModel.loadingState.value = ViewState.Loaded
                binding.filtersList.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })

        val itemAdapter =
            ItemsFiltersListAdapter(ViewFilters.AllFilters.values(), viewModel, lifecycleScope)

        itemAdapter.setHasStableIds(true)

        binding.filtersList.setHasFixedSize(true)
        binding.filtersList.layoutManager = LinearLayoutManager(view.context)
        binding.filtersList.adapter = itemAdapter
        binding.filtersList.setItemViewCacheSize(25)
        binding.filtersList.addItemDecoration(divider)
    }
}