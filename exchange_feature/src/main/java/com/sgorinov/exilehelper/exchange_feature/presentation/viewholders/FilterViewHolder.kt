package com.sgorinov.exilehelper.exchange_feature.presentation.viewholders

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sgorinov.exilehelper.core.presentation.SlideUpDownAnimator
import com.sgorinov.exilehelper.core.presentation.hideKeyboard
import com.sgorinov.exilehelper.core.presentation.measureForAnimator
import com.sgorinov.exilehelper.exchange_feature.R
import com.sgorinov.exilehelper.exchange_feature.data.models.LocalFilter
import com.sgorinov.exilehelper.exchange_feature.databinding.FilterHeaderViewBinding
import com.sgorinov.exilehelper.exchange_feature.presentation.adapters.ItemsFilterAdapter
import com.sgorinov.exilehelper.exchange_feature.presentation.models.enums.ViewFilters

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

    fun bind(
        item: ViewFilters.Filter,
        filters: MutableList<LocalFilter>,
        isExpanded: Boolean,
        onExpand: (Boolean) -> Unit
    ) {
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
                    onExpand(false)
                }
                View.GONE -> {
                    animator.slideDown()
                    onExpand(true)
                }
            }
        }

        viewBinding.filterEnabled.setOnCheckedChangeListener { _, checked ->
            filter.isEnabled = checked
            animator.setHeight(viewBinding.filterItemsLayout.measureForAnimator())
            when {
                !checked && viewBinding.filterItemsLayout.visibility == View.VISIBLE -> {
                    animator.slideUp()
                    onExpand(false)
                }
                checked && viewBinding.filterItemsLayout.visibility == View.GONE -> {
                    animator.slideDown()
                    onExpand(true)
                }
            }
        }

        if (isExpanded) {
            viewBinding.filterItemsLayout.visibility = View.VISIBLE
        } else {
            viewBinding.filterItemsLayout.visibility = View.GONE
        }
    }

    private fun getOrCreateFilter(
        filters: MutableList<LocalFilter>,
        id: String,
        onFieldsChanged: (Boolean) -> Unit
    ): LocalFilter {
        val existingFilter = filters.firstOrNull { it.name == id }
        return if (existingFilter == null) {
            val newFilter = LocalFilter(id, onFieldsChanged)
            filters.add(newFilter)
            newFilter
        } else {
            existingFilter.apply { this.onFieldsChanged = onFieldsChanged }
        }
    }
}