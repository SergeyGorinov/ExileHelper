package com.poe.tradeapp.exchange.presentation.adapters

import android.view.View
import android.widget.CheckBox
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.poe.tradeapp.core.presentation.SlideUpDownAnimator
import com.poe.tradeapp.core.presentation.hideKeyboard
import com.poe.tradeapp.core.presentation.measureForAnimator
import com.poe.tradeapp.exchange.R
import com.poe.tradeapp.exchange.presentation.ItemsSearchActivity
import com.poe.tradeapp.exchange.presentation.models.Filter
import com.poe.tradeapp.exchange.presentation.models.enums.ViewFilters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@ExperimentalCoroutinesApi
class FilterViewHolder(itemView: View, private val scope: CoroutineScope) :
    RecyclerView.ViewHolder(itemView), IBindableViewHolder {

    private val enabled: CheckBox = itemView.findViewById(R.id.filterEnabled)
    private val showHide: MaterialButton = itemView.findViewById(R.id.filterShowHideButton)
    private val filterItemsLayout: RecyclerView = itemView.findViewById(R.id.filterItemsLayout)
    private val clearAll: ImageButton = itemView.findViewById(R.id.filterClearAll)
    private val context = itemView.context as ItemsSearchActivity
    private val animator = SlideUpDownAnimator(filterItemsLayout)

    init {
        val divider = DividerItemDecoration(context, RecyclerView.VERTICAL)
        ContextCompat.getDrawable(context, R.drawable.table_column_divider)
            ?.let { divider.setDrawable(it) }

        filterItemsLayout.layoutManager = LinearLayoutManager(context)
        filterItemsLayout.setHasFixedSize(true)
        filterItemsLayout.addItemDecoration(divider)
    }

    override fun bind(item: ViewFilters.AllFilters, filter: Filter) {
        enabled.isChecked = filter.isEnabled
        showHide.text = item.text

        filter.isFilterEmpty.onEach {
            clearAll.visibility = if (it) View.GONE else View.VISIBLE
        }.launchIn(scope)

        clearAll.setOnClickListener {
            filter.cleanFiler()
            filterItemsLayout.adapter = ItemsFilterAdapter(item.values, filter)
            context.hideKeyboard(it)
        }

        enabled.setOnCheckedChangeListener { _, checked ->
            if (!checked && filterItemsLayout.visibility == View.VISIBLE)
                animator.slideUp()
            if (checked && filterItemsLayout.visibility == View.GONE)
                animator.slideDown()
            filter.isEnabled = checked
        }

        showHide.setOnClickListener {
            if (filterItemsLayout.visibility == View.VISIBLE) {
                animator.slideUp()
            } else {
                if (!filter.isEnabled) {
                    filter.isEnabled = true
                    enabled.isChecked = filter.isEnabled
                }
                animator.slideDown()
            }
        }

        filterItemsLayout.adapter = ItemsFilterAdapter(item.values, filter)

        animator.setHeight(filterItemsLayout.measureForAnimator())
    }
}