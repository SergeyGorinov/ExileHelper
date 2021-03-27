package com.poe.tradeapp.exchange.presentation.viewholders

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.poe.tradeapp.core.presentation.SlideUpDownAnimator
import com.poe.tradeapp.core.presentation.hideKeyboard
import com.poe.tradeapp.core.presentation.measureForAnimator
import com.poe.tradeapp.exchange.R
import com.poe.tradeapp.exchange.databinding.FiltersHeaderItemBinding
import com.poe.tradeapp.exchange.presentation.adapters.ItemsFilterAdapter
import com.poe.tradeapp.exchange.presentation.models.Filter
import com.poe.tradeapp.exchange.presentation.models.enums.ViewFilters

internal class FilterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    IBindableViewHolder {

    private val viewBinding = FiltersHeaderItemBinding.bind(itemView)
    private val animator = SlideUpDownAnimator(viewBinding.filterItemsLayout)

    init {
        val divider = DividerItemDecoration(itemView.context, RecyclerView.VERTICAL)
        ContextCompat.getDrawable(itemView.context, R.drawable.table_column_divider)
            ?.let { divider.setDrawable(it) }

        viewBinding.filterItemsLayout.layoutManager = LinearLayoutManager(itemView.context)
        viewBinding.filterItemsLayout.setHasFixedSize(true)
        viewBinding.filterItemsLayout.addItemDecoration(divider)
    }

    override fun bind(item: ViewFilters.AllFilters, filters: MutableList<Filter>) {
        var filter = filters.firstOrNull { it.name == item.id }
        if (filter == null) {
            filter = Filter(item.id) {
                viewBinding.filterClearAll.visibility = if (it) View.GONE else View.VISIBLE
            }
            filters.add(filter)
        }

        viewBinding.filterEnabled.isChecked = filter.isEnabled
        viewBinding.filterShowHideButton.text = item.text

        viewBinding.filterClearAll.setOnClickListener {
            filter.cleanFilter()
            viewBinding.filterItemsLayout.adapter = ItemsFilterAdapter(item.values, filter)
            itemView.context.hideKeyboard(it)
        }

        viewBinding.filterEnabled.setOnCheckedChangeListener { _, checked ->
            if (!checked && viewBinding.filterItemsLayout.visibility == View.VISIBLE)
                animator.slideUp()
            if (checked && viewBinding.filterItemsLayout.visibility == View.GONE)
                animator.slideDown()
            filter.isEnabled = checked
        }

        viewBinding.filterShowHideButton.setOnClickListener {
            if (viewBinding.filterItemsLayout.adapter == null) {
                viewBinding.filterItemsLayout.adapter = ItemsFilterAdapter(item.values, filter)
                animator.setHeight(viewBinding.filterItemsLayout.measureForAnimator())
            }
            if (viewBinding.filterItemsLayout.visibility == View.VISIBLE) {
                animator.slideUp()
            } else {
                if (!filter.isEnabled) {
                    filter.isEnabled = true
                    viewBinding.filterEnabled.isChecked = filter.isEnabled
                }
                animator.slideDown()
            }
        }
    }
}