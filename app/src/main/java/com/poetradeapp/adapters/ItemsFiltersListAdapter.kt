package com.poetradeapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.poetradeapp.R
import com.poetradeapp.activities.ItemsSearchActivity
import com.poetradeapp.models.enums.ViewFilters
import com.poetradeapp.models.ui.Filter
import com.poetradeapp.models.view.ItemsSearchViewModel
import com.poetradeapp.ui.SlideUpDownAnimator
import com.poetradeapp.ui.hideKeyboard
import com.poetradeapp.ui.measureForAnimator
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

@ExperimentalCoroutinesApi
class FilterListAdapter(
    private val items: Array<ViewFilters.AllFilters>,
    private val viewModel: ItemsSearchViewModel
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val view =
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.filters_header_item, parent, false)
        return ViewHolderViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is IBindableViewHolder) {
            val filterType = items[position]

            var filter = viewModel.getFilter(filterType.id)

            if (filter == null) {
                filter = Filter(filterType.id)
                viewModel.addFilter(filter)
            }

            holder.bind(filterType, filter)
        }
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int) = items[position].ordinal

    override fun getItemId(position: Int) = items[position].hashCode().toLong()
}

@ExperimentalCoroutinesApi
class ViewHolderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    IBindableViewHolder {
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

        GlobalScope.launch {
            filter.isFilterEmpty.collect {
                withContext(Dispatchers.Main) {
                    clearAll.visibility = if (it) View.GONE else View.VISIBLE
                }
            }
        }

        clearAll.setOnClickListener {
            filter.cleanFiler()
            filterItemsLayout.adapter = ItemFilterAdapter(item.values, filter)
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

        filterItemsLayout.adapter = ItemFilterAdapter(item.values, filter)

        animator.setHeight(filterItemsLayout.measureForAnimator())
    }
}