package com.sgorinov.exilehelper.exchange_feature.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sgorinov.exilehelper.exchange_feature.R
import com.sgorinov.exilehelper.exchange_feature.data.models.LocalFilter
import com.sgorinov.exilehelper.exchange_feature.presentation.models.enums.ViewFilters
import com.sgorinov.exilehelper.exchange_feature.presentation.viewholders.FilterViewHolder

internal class ItemsFiltersListAdapter(
    private val items: MutableMap<ViewFilters.Filter, Boolean>,
    private val filters: MutableList<LocalFilter>
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
        val item = items.entries.elementAt(position)
        holder.bind(item.key, filters, item.value) {
            item.setValue(it)
        }
    }

    override fun getItemCount() = items.size
}