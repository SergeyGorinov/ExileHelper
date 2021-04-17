package com.sgorinov.exilehelper.exchange.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.sgorinov.exilehelper.core.presentation.SlideUpDownAnimator
import com.sgorinov.exilehelper.exchange.R
import com.sgorinov.exilehelper.exchange.data.models.Filter
import com.sgorinov.exilehelper.exchange.databinding.HeistFiltersViewBinding
import com.sgorinov.exilehelper.exchange.presentation.models.enums.ViewFilters

internal class HeistFiltersView(ctx: Context, attrs: AttributeSet) :
    BaseExpandableView(ctx, attrs) {

    override val animator: SlideUpDownAnimator

    var viewBinding: HeistFiltersViewBinding?

    init {
        val view = LayoutInflater.from(ctx).inflate(R.layout.heist_filters_view, this, true)
        animator = SlideUpDownAnimator(view)
        viewBinding = HeistFiltersViewBinding.bind(view)
    }

    override fun setupFields(filter: Filter) {
        viewBinding?.let {
            it.contractObjectiveValueDropdown.setupDropDown(
                filter.getOrCreateField(ViewFilters.HeistFilters.ContractObjectiveValue.id),
                ViewFilters.HeistFilters.ContractObjectiveValue.dropDownValues?.toList() ?: listOf()
            )
            it.areaLevelMinMax.setupMinMax(
                filter.getOrCreateField(ViewFilters.HeistFilters.AreaLevel.id)
            )
            it.wingsRevealedMinMax.setupMinMax(
                filter.getOrCreateField(ViewFilters.HeistFilters.WingsRevealed.id)
            )
            it.totalWingsMinMax.setupMinMax(
                filter.getOrCreateField(ViewFilters.HeistFilters.TotalWings.id)
            )
            it.escapeRoutesRevealedMinMax.setupMinMax(
                filter.getOrCreateField(ViewFilters.HeistFilters.EscapeRoutesRevealed.id)
            )
            it.totalEscapeRoutesMinMax.setupMinMax(
                filter.getOrCreateField(ViewFilters.HeistFilters.TotalEscapeRoutes.id)
            )
            it.rewardRoomsRevealedMinMax.setupMinMax(
                filter.getOrCreateField(ViewFilters.HeistFilters.RewardRoomsRevealed.id)
            )
            it.totalRewardRoomsMinMax.setupMinMax(
                filter.getOrCreateField(ViewFilters.HeistFilters.TotalRewardRooms.id)
            )
            it.lockpickingLevelMinMax.setupMinMax(
                filter.getOrCreateField(ViewFilters.HeistFilters.LockpickingLevel.id)
            )
            it.bruteForceLevelMinMax.setupMinMax(
                filter.getOrCreateField(ViewFilters.HeistFilters.BruteForceLevel.id)
            )
            it.perceptionLevelMinMax.setupMinMax(
                filter.getOrCreateField(ViewFilters.HeistFilters.PerceptionLevel.id)
            )
            it.demolitionLevelMinMax.setupMinMax(
                filter.getOrCreateField(ViewFilters.HeistFilters.DemolitionLevel.id)
            )
            it.counterThaumLevelMinMax.setupMinMax(
                filter.getOrCreateField(ViewFilters.HeistFilters.CounterThaumLevel.id)
            )
            it.trapDisarmamentLevelMinMax.setupMinMax(
                filter.getOrCreateField(ViewFilters.HeistFilters.TrapDisarmamentLevel.id)
            )
            it.agilityLevelMinMax.setupMinMax(
                filter.getOrCreateField(ViewFilters.HeistFilters.AgilityLevel.id)
            )
            it.deceptionLevelMinMax.setupMinMax(
                filter.getOrCreateField(ViewFilters.HeistFilters.DeceptionLevel.id)
            )
            it.engineeringLevelMinMax.setupMinMax(
                filter.getOrCreateField(ViewFilters.HeistFilters.EngineeringLevel.id)
            )
        }
    }

    override fun cleanFields() {
        viewBinding?.let {
            it.contractObjectiveValueDropdown.cleanField()
            it.areaLevelMinMax.cleanField()
            it.wingsRevealedMinMax.cleanField()
            it.totalWingsMinMax.cleanField()
            it.escapeRoutesRevealedMinMax.cleanField()
            it.totalEscapeRoutesMinMax.cleanField()
            it.rewardRoomsRevealedMinMax.cleanField()
            it.totalRewardRoomsMinMax.cleanField()
            it.lockpickingLevelMinMax.cleanField()
            it.bruteForceLevelMinMax.cleanField()
            it.perceptionLevelMinMax.cleanField()
            it.demolitionLevelMinMax.cleanField()
            it.counterThaumLevelMinMax.cleanField()
            it.trapDisarmamentLevelMinMax.cleanField()
            it.agilityLevelMinMax.cleanField()
            it.deceptionLevelMinMax.cleanField()
            it.engineeringLevelMinMax.cleanField()
        }
    }

    override fun onViewRemoved(view: View?) {
        super.onViewRemoved(view)
        viewBinding = null
    }
}