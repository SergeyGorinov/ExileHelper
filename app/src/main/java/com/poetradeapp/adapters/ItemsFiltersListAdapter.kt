package com.poetradeapp.adapters

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.poetradeapp.R
import com.poetradeapp.activities.ItemsSearchActivity
import com.poetradeapp.listeners.EnableDisableFilterListener
import com.poetradeapp.models.enums.ViewFilters
import com.poetradeapp.models.enums.getFilterByType
import com.poetradeapp.models.requestmodels.Clearable
import com.poetradeapp.models.viewmodels.ItemsSearchViewModel
import com.poetradeapp.ui.SlideUpDownAnimator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class ItemFiltersListAdapter(private val items: Array<ViewFilters.AllFilters>) :
    RecyclerView.Adapter<ItemFiltersListRecyclerViewVH>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemFiltersListRecyclerViewVH {
        val view =
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.filters_main_item, parent, false)

        return ItemFiltersListRecyclerViewVH(view)
    }

    override fun onBindViewHolder(holder: ItemFiltersListRecyclerViewVH, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}

@ExperimentalCoroutinesApi
class ItemFiltersListRecyclerViewVH(itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    private val enabled: CheckBox = itemView.findViewById(R.id.filterEnabled)
    private val showHide: MaterialButton = itemView.findViewById(R.id.filterShowHideButton)
    private val filterItemsLayout: RecyclerView = itemView.findViewById(R.id.filterItemsLayout)
    private val clearAll: ImageButton = itemView.findViewById(R.id.filterClearAll)
    private val context = itemView.context as ItemsSearchActivity
    private val animator = SlideUpDownAnimator(filterItemsLayout)
    internal val filters by lazy {
        ViewModelProvider(
            context,
            ViewModelProvider.AndroidViewModelFactory(context.application)
        ).get(ItemsSearchViewModel::class.java).getItemRequestData().query.filters
    }

    init {
        showHide.setOnClickListener {
            if (filterItemsLayout.visibility == View.VISIBLE)
                animator.slideUp()
            else
                animator.slideDown()
        }

        val divider = DividerItemDecoration(context, RecyclerView.VERTICAL)
        ContextCompat.getDrawable(context, R.drawable.table_column_divider)
            ?.let { divider.setDrawable(it) }

        filterItemsLayout.layoutManager = LinearLayoutManager(context)
        filterItemsLayout.setHasFixedSize(true)
        filterItemsLayout.addItemDecoration(divider)
    }

    fun bind(item: ViewFilters.AllFilters) {
        showHide.text = item.text
        val filter = item.getFilterByType(filters)
        enabled.isChecked = !filter.disabled
        enabled.setOnCheckedChangeListener(EnableDisableFilterListener(filter))

        if (filter is Clearable) {
            clearAll.setOnClickListener {
                filter.clearAll()
                filterItemsLayout.adapter =
                    ItemFilterAdapter(item)
            }
            GlobalScope.launch {
                filter.isEmptyState.collect {
                    GlobalScope.launch(Dispatchers.Main) {
                        clearAll.visibility = if (it) View.GONE else View.VISIBLE
                    }
                }
            }
        }
        filterItemsLayout.adapter = ItemFilterAdapter(item)

        val layoutParams = filterItemsLayout.layoutParams
        layoutParams.height = 1
        filterItemsLayout.layoutParams = layoutParams

        filterItemsLayout.measure(
            View.MeasureSpec.makeMeasureSpec(
                Resources.getSystem().displayMetrics.widthPixels,
                View.MeasureSpec.EXACTLY
            ),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )

        animator.setHeight(filterItemsLayout.measuredHeight)
    }
}