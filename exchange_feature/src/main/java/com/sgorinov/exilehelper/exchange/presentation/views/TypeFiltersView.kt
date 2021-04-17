package com.sgorinov.exilehelper.exchange.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.sgorinov.exilehelper.core.presentation.SlideUpDownAnimator
import com.sgorinov.exilehelper.exchange.R
import com.sgorinov.exilehelper.exchange.data.models.Filter
import com.sgorinov.exilehelper.exchange.databinding.TypeFiltersViewBinding
import com.sgorinov.exilehelper.exchange.presentation.models.enums.ViewFilters

internal class TypeFiltersView(ctx: Context, attrs: AttributeSet) : BaseExpandableView(ctx, attrs) {

    override val animator: SlideUpDownAnimator

    var viewBinding: TypeFiltersViewBinding?

    init {
        val view = LayoutInflater.from(ctx).inflate(R.layout.type_filters_view, this, true)
        viewBinding = TypeFiltersViewBinding.bind(view)
        animator = SlideUpDownAnimator(view)
    }

    override fun setupFields(filter: Filter) {
        viewBinding?.let {
            it.itemCategoryDropDown.setupDropDown(
                filter.getOrCreateField(ViewFilters.TypeFilters.Category.id),
                ViewFilters.TypeFilters.Category.dropDownValues?.toList() ?: listOf()
            )
            it.itemRarityDropDown.setupDropDown(
                filter.getOrCreateField(ViewFilters.TypeFilters.Rarity.id),
                ViewFilters.TypeFilters.Rarity.dropDownValues?.toList() ?: listOf()
            )
        }
    }

    override fun cleanFields() {
        viewBinding?.let {
            it.itemCategoryDropDown.cleanField()
            it.itemRarityDropDown.cleanField()
        }
    }

    override fun onViewRemoved(view: View?) {
        super.onViewRemoved(view)
        viewBinding = null
    }
}