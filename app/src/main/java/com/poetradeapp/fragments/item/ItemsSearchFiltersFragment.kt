package com.poetradeapp.fragments.item

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.poetradeapp.R
import com.poetradeapp.adapters.FilterListAdapter
import com.poetradeapp.models.enums.ViewFilters
import com.poetradeapp.models.enums.ViewState
import com.poetradeapp.models.view.ItemsSearchViewModel
import kotlinx.android.synthetic.main.fragment_item_exchange_filters.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

@ExperimentalCoroutinesApi
class ItemsSearchFiltersFragment : Fragment() {

    internal val viewModel: ItemsSearchViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_item_exchange_filters, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val divider = DividerItemDecoration(view.context, RecyclerView.VERTICAL)
        ContextCompat.getDrawable(view.context, R.drawable.table_column_divider)
            ?.let { divider.setDrawable(it) }

        filtersList.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewModel.loadingState.value = ViewState.Loaded
                filtersList.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })

        val itemAdapter = FilterListAdapter(ViewFilters.AllFilters.values(), viewModel)

        itemAdapter.setHasStableIds(true)

        filtersList.setHasFixedSize(true)
        filtersList.layoutManager = LinearLayoutManager(view.context)
        filtersList.adapter = itemAdapter
        filtersList.setItemViewCacheSize(25)
        filtersList.addItemDecoration(divider)
    }
}