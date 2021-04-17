package com.sgorinov.exilehelper.exchange.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.sgorinov.exilehelper.core.presentation.SlideUpDownAnimator
import com.sgorinov.exilehelper.exchange.R
import com.sgorinov.exilehelper.exchange.data.models.Filter
import com.sgorinov.exilehelper.exchange.databinding.RequirementsFiltersViewBinding
import com.sgorinov.exilehelper.exchange.presentation.models.enums.ViewFilters

internal class RequirementsFiltersView(ctx: Context, attrs: AttributeSet) :
    BaseExpandableView(ctx, attrs) {

    override val animator: SlideUpDownAnimator

    private var viewBinding: RequirementsFiltersViewBinding?

    init {
        val view = LayoutInflater.from(ctx).inflate(R.layout.requirements_filters_view, this, true)
        viewBinding = RequirementsFiltersViewBinding.bind(view)
        animator = SlideUpDownAnimator(view)
    }

    override fun setupFields(filter: Filter) {
        viewBinding?.let {
            it.level.setupMinMax(
                filter.getOrCreateField(ViewFilters.ReqFilters.Level.id)
            )
            it.strength.setupMinMax(
                filter.getOrCreateField(ViewFilters.ReqFilters.Strength.id)
            )
            it.dexterity.setupMinMax(
                filter.getOrCreateField(ViewFilters.ReqFilters.Dexterity.id)
            )
            it.intelligence.setupMinMax(
                filter.getOrCreateField(ViewFilters.ReqFilters.Intelligence.id)
            )
        }
    }

    override fun cleanFields() {
        viewBinding?.let {
            it.level.cleanField()
            it.strength.cleanField()
            it.dexterity.cleanField()
            it.intelligence.cleanField()
        }
    }

    override fun onViewRemoved(view: View?) {
        super.onViewRemoved(view)
        viewBinding = null
    }
}