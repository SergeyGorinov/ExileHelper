package com.poe.tradeapp.exchange.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.poe.tradeapp.exchange.R
import com.poe.tradeapp.exchange.presentation.models.Filter
import com.poe.tradeapp.exchange.presentation.models.enums.ViewFilters
import com.poe.tradeapp.exchange.presentation.viewholders.FilterViewHolder
import com.poe.tradeapp.exchange.presentation.viewholders.IBindableViewHolder

internal class ItemsFiltersListAdapter(
    private val items: Array<ViewFilters.AllFilters>,
    private val filters: MutableList<Filter>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val view =
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.filters_header_item, parent, false)
        return FilterViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is IBindableViewHolder) {
            holder.bind(items[position], filters)
        }
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int) = items[position].ordinal

    override fun getItemId(position: Int) = items[position].hashCode().toLong()
}