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

    init {
        val divider = DividerItemDecoration(itemView.context, RecyclerView.VERTICAL)
        ContextCompat.getDrawable(itemView.context, R.drawable.table_column_divider)
            ?.let { divider.setDrawable(it) }

        viewBinding.filterItemsLayout.layoutManager = LinearLayoutManager(itemView.context)
        viewBinding.filterItemsLayout.addItemDecoration(divider)
    }

    fun bind(item: ViewFilters.Filter, filters: MutableList<Filter>) {
        val filter = getOrCreateFilter(filters, item.id) {
            viewBinding.filterClearAll.visibility = if (it) View.GONE else View.VISIBLE
        }

        viewBinding.filterShowHideButton.text = item.text
        viewBinding.filterEnabled.isChecked = filter.isEnabled
        viewBinding.filterItemsLayout.adapter = ItemsFilterAdapter(item.values, filter)

        viewBinding.filterClearAll.setOnClickListener {
            filter.cleanFilter()
            viewBinding.filterItemsLayout.adapter?.notifyDataSetChanged()
            itemView.context.hideKeyboard(it)
        }

        viewBinding.filterShowHideButton.setOnClickListener {
            animator.setHeight(viewBinding.filterItemsLayout.measureForAnimator())
            when (viewBinding.filterItemsLayout.visibility) {
                View.VISIBLE -> {
                    animator.slideUp()
                }
                View.GONE -> {
                    animator.slideDown()
                }
            }
        }

        viewBinding.filterEnabled.setOnCheckedChangeListener { _, checked ->
            filter.isEnabled = checked
            when {
                !checked && viewBinding.filterItemsLayout.visibility == View.VISIBLE -> {
                    animator.slideUp()
                }
                checked && viewBinding.filterItemsLayout.visibility == View.GONE -> {
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
            existingFilter.apply { this.onFieldsChanged = onFieldsChanged }
        }
    }
}