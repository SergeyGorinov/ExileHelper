package com.sgorinov.exilehelper.exchange.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sgorinov.exilehelper.exchange.R
import com.sgorinov.exilehelper.exchange.data.models.Filter
import com.sgorinov.exilehelper.exchange.presentation.models.enums.ViewFilters
import com.sgorinov.exilehelper.exchange.presentation.viewholders.FilterViewHolder

internal class ItemsFiltersListAdapter(
    private val items: Array<ViewFilters.AllFilters>,
    private val filters: MutableList<Filter>
) : RecyclerView.Adapter<FilterViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FilterViewHolder {
        val view =
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.filter_header_view, parent, false)
        return FilterViewHolder(view)
    }

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        holder.bind(items[position], filters)
    }

    override fun getItemCount() = items.size
}