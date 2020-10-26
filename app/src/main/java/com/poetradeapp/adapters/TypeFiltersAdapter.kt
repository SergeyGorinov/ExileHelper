package com.poetradeapp.adapters

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.poetradeapp.R
import com.poetradeapp.activities.ItemsSearchActivity
import com.poetradeapp.listeners.DropDownChangedListener
import com.poetradeapp.listeners.EnableDisableFilterListener
import com.poetradeapp.models.enums.Dropdowns
import com.poetradeapp.models.enums.ViewFilters
import com.poetradeapp.models.viewmodels.ItemsSearchViewModel
import com.poetradeapp.ui.SlideUpDownAnimator
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class TypeFiltersAdapter : RecyclerView.Adapter<TypeFiltersViewHolder>() {

    override fun getItemCount() = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypeFiltersViewHolder {
        val view =
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.filters_type_item, parent, false)

        return TypeFiltersViewHolder(view)
    }

    override fun onBindViewHolder(holder: TypeFiltersViewHolder, position: Int) = Unit
}

@ExperimentalCoroutinesApi
class TypeFiltersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val enabled: CheckBox = itemView.findViewById(R.id.filterEnabled)
    private val showHide: MaterialButton = itemView.findViewById(R.id.filterShowHideButton)
    private val filterItemsLayout: LinearLayout = itemView.findViewById(R.id.filterItemsLayout)
    private val clearAll: ImageButton = itemView.findViewById(R.id.filterClearAll)
    private val itemCategory =
        itemView.findViewById<AutoCompleteTextView>(R.id.itemCategoryDropDown)
    private val itemRarity =
        itemView.findViewById<AutoCompleteTextView>(R.id.itemRarityDropDown)
    private val context = itemView.context as ItemsSearchActivity
    private val animator = SlideUpDownAnimator(filterItemsLayout)
    private val filters by lazy {
        ViewModelProvider(
            context,
            ViewModelProvider.AndroidViewModelFactory(context.application)
        ).get(ItemsSearchViewModel::class.java).getItemRequestData().query.filters
    }

    init {
        val filter = filters.type_filters

        enabled.isChecked = !filter.disabled
        enabled.setOnCheckedChangeListener(EnableDisableFilterListener(filter))

        showHide.setOnClickListener {
            if (filterItemsLayout.visibility == View.VISIBLE)
                animator.slideUp()
            else
                animator.slideDown()
        }

        showHide.text = context.getString(R.string.type_filter_header)

        itemCategory.setAdapter(
            DropDownAdapter(
                itemView.context,
                R.layout.dropdown_item,
                R.id.itemText,
                Dropdowns.ItemCategory.values().toList()
            )
        )
        itemCategory.setText(Dropdowns.ItemCategory.Any.text, false)
        itemRarity.setAdapter(
            DropDownAdapter(
                itemView.context,
                R.layout.dropdown_item,
                R.id.itemText,
                Dropdowns.ItemRarity.values().toList()
            )
        )
        itemRarity.setText(Dropdowns.ItemRarity.Any.text, false)
        itemCategory.onItemClickListener =
            DropDownChangedListener(ViewFilters.TypeFilters.Category, filters)
        itemRarity.onItemClickListener =
            DropDownChangedListener(ViewFilters.TypeFilters.Rarity, filters)

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