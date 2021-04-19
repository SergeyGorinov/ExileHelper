package com.sgorinov.exilehelper.exchange.presentation.viewholders

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sgorinov.exilehelper.core.presentation.SlideUpDownAnimator
import com.sgorinov.exilehelper.core.presentation.hideKeyboard
import com.sgorinov.exilehelper.core.presentation.measureForAnimator
import com.sgorinov.exilehelper.exchange.R
import com.sgorinov.exilehelper.exchange.data.models.Filter
import com.sgorinov.exilehelper.exchange.databinding.FilterHeaderViewBinding
import com.sgorinov.exilehelper.exchange.presentation.adapters.ItemsFilterAdapter
import com.sgorinov.exilehelper.exchange.presentation.models.enums.ViewFilters

internal class FilterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val viewBinding = FilterHeaderViewBinding.bind(itemView)
    private val animator = SlideUpDownAnimator(viewBinding.filterItemsLayout)

    fun bind(item: ViewFilters.AllFilters, filters: MutableList<Filter>) {
        val filter = getOrCreateFilter(filters, item.id) {
            viewBinding.filterClearAll.visibility = if (it) View.GONE else View.VISIBLE
        }

        viewBinding.filterEnabled.isChecked = filter.isEnabled
        viewBinding.filterShowHideButton.text = item.text

        viewBinding.filterClearAll.setOnClickListener {
            filter.cleanFilter()
            viewBinding.filterItemsLayout.adapter = ItemsFilterAdapter(item.values, filter)
            itemView.context.hideKeyboard(it)
        }

        viewBinding.filterEnabled.setOnCheckedChangeListener { _, checked ->
            when {
                !checked && viewBinding.filterItemsLayout.visibility == View.VISIBLE -> {
                    animator.slideUp()
                }
                checked && viewBinding.filterItemsLayout.visibility == View.GONE -> {
                    animator.slideDown()
                }
            }
            filter.isEnabled = checked
        }

        viewBinding.filterShowHideButton.setOnClickListener {
            if (viewBinding.filterItemsLayout.adapter == null) {
                val divider = DividerItemDecoration(itemView.context, RecyclerView.VERTICAL)
                ContextCompat.getDrawable(itemView.context, R.drawable.table_column_divider)
                    ?.let { divider.setDrawable(it) }

                viewBinding.filterItemsLayout.layoutManager = LinearLayoutManager(itemView.context)
                viewBinding.filterItemsLayout.setHasFixedSize(true)
                viewBinding.filterItemsLayout.addItemDecoration(divider)
                viewBinding.filterItemsLayout.adapter = ItemsFilterAdapter(item.values, filter)
                animator.setHeight(viewBinding.filterItemsLayout.measureForAnimator())
            }
            if (viewBinding.filterItemsLayout.visibility == View.VISIBLE) {
                animator.slideUp()
            } else {
                if (!filter.isEnabled) {
                    filter.isEnabled = true
                    viewBinding.filterEnabled.isChecked = filter.isEnabled
                } else {
                    animator.slideDown()
                }
            }
        }
    }

    private fun getOrCreateFilter(
        filters: MutableList<Filter>,
        id: String,
        onFieldsChanged: (Boolean) -> Unit
    ): Filter {
        val existingFilter = filters.firstOrNull { it.name == id }
        return if (existingFilter == null) {
            val newFilter = Filter(id, onFieldsChanged)
            filters.add(newFilter)
            newFilter
        } else {
            existingFilter
        }
    }
}