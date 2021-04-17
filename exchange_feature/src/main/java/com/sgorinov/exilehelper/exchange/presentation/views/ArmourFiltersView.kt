package com.sgorinov.exilehelper.exchange.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.sgorinov.exilehelper.core.presentation.SlideUpDownAnimator
import com.sgorinov.exilehelper.exchange.R
import com.sgorinov.exilehelper.exchange.data.models.Filter
import com.sgorinov.exilehelper.exchange.databinding.ArmourFiltersViewBinding
import com.sgorinov.exilehelper.exchange.presentation.models.enums.ViewFilters

internal class ArmourFiltersView(ctx: Context, attrs: AttributeSet) :
    BaseExpandableView(ctx, attrs) {

    override val animator: SlideUpDownAnimator

    private var viewBinding: ArmourFiltersViewBinding?

    init {
        val view = LayoutInflater.from(ctx).inflate(R.layout.armour_filters_view, this, true)
        viewBinding = ArmourFiltersViewBinding.bind(view)
        animator = SlideUpDownAnimator(view)
    }

    override fun setupFields(filter: Filter) {
        viewBinding?.let {
            it.armour.setupMinMax(
                filter.getOrCreateField(ViewFilters.ArmourFilters.Armour.id)
            )
            it.evasion.setupMinMax(
                filter.getOrCreateField(ViewFilters.ArmourFilters.Evasion.id)
            )
            it.energyShield.setupMinMax(
                filter.getOrCreateField(ViewFilters.ArmourFilters.EnergyShield.id)
            )
            it.block.setupMinMax(
                filter.getOrCreateField(ViewFilters.ArmourFilters.Block.id)
            )
        }
    }

    override fun cleanFields() {
        viewBinding?.let {
            it.armour.cleanField()
            it.evasion.cleanField()
            it.energyShield.cleanField()
            it.block.cleanField()
        }
    }

    override fun onViewRemoved(view: View?) {
        super.onViewRemoved(view)
        viewBinding = null
    }
}