package com.sgorinov.exilehelper.exchange.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.sgorinov.exilehelper.core.presentation.SlideUpDownAnimator
import com.sgorinov.exilehelper.exchange.R
import com.sgorinov.exilehelper.exchange.data.models.Filter
import com.sgorinov.exilehelper.exchange.databinding.WeaponFiltersViewBinding
import com.sgorinov.exilehelper.exchange.presentation.models.enums.ViewFilters

internal class WeaponFiltersView(ctx: Context, attrs: AttributeSet) :
    BaseExpandableView(ctx, attrs) {

    override val animator: SlideUpDownAnimator

    private var viewBinding: WeaponFiltersViewBinding?

    init {
        val view = LayoutInflater.from(ctx).inflate(R.layout.weapon_filters_view, this, true)
        viewBinding = WeaponFiltersViewBinding.bind(view)
        animator = SlideUpDownAnimator(view)
    }

    override fun setupFields(filter: Filter) {
        viewBinding?.let {
            it.damage.setupMinMax(
                filter.getOrCreateField(ViewFilters.WeaponFilters.Damage.id)
            )
            it.criticalChance.setupMinMax(
                filter.getOrCreateField(ViewFilters.WeaponFilters.CritChance.id)
            )
            it.physicalDps.setupMinMax(
                filter.getOrCreateField(ViewFilters.WeaponFilters.PDPS.id)
            )
            it.attacksPerSecond.setupMinMax(
                filter.getOrCreateField(ViewFilters.WeaponFilters.APS.id)
            )
            it.damagePerSecond.setupMinMax(
                filter.getOrCreateField(ViewFilters.WeaponFilters.DPS.id)
            )
            it.elementalDps.setupMinMax(
                filter.getOrCreateField(ViewFilters.WeaponFilters.EDPS.id)
            )
        }
    }

    override fun cleanFields() {
        viewBinding?.let {
            it.damage.cleanField()
            it.criticalChance.cleanField()
            it.physicalDps.cleanField()
            it.attacksPerSecond.cleanField()
            it.damagePerSecond.cleanField()
            it.elementalDps.cleanField()
        }
    }

    override fun onViewRemoved(view: View?) {
        super.onViewRemoved(view)
        viewBinding = null
    }
}