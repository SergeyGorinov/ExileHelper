package com.sgorinov.exilehelper.exchange.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.sgorinov.exilehelper.core.presentation.SlideUpDownAnimator
import com.sgorinov.exilehelper.exchange.R
import com.sgorinov.exilehelper.exchange.data.models.Filter
import com.sgorinov.exilehelper.exchange.databinding.MapFiltersViewBinding
import com.sgorinov.exilehelper.exchange.presentation.models.enums.ViewFilters

internal class MapFiltersView(ctx: Context, attrs: AttributeSet) : BaseExpandableView(ctx, attrs) {

    override val animator: SlideUpDownAnimator

    private var viewBinding: MapFiltersViewBinding?

    init {
        val view = LayoutInflater.from(ctx).inflate(R.layout.map_filters_view, this, true)
        viewBinding = MapFiltersViewBinding.bind(view)
        animator = SlideUpDownAnimator(view)
    }

    override fun setupFields(filter: Filter) {
        viewBinding?.let {
            it.mapTierMinMax.setupMinMax(
                filter.getOrCreateField(ViewFilters.MapFilters.MapTier.id)
            )
            it.mapPacksizeMinMax.setupMinMax(
                filter.getOrCreateField(ViewFilters.MapFilters.MapPacksize.id)
            )
            it.mapIiqMinMax.setupMinMax(
                filter.getOrCreateField(ViewFilters.MapFilters.MapIIQ.id)
            )
            it.mapIirMinMax.setupMinMax(
                filter.getOrCreateField(ViewFilters.MapFilters.MapIIR.id)
            )
            it.mapSeriesDropDown.setupDropDown(
                filter.getOrCreateField(ViewFilters.MapFilters.MapSeries.id),
                ViewFilters.MapFilters.MapSeries.dropDownValues?.toList() ?: listOf()
            )
            it.mapRegionDropDown.setupDropDown(
                filter.getOrCreateField(ViewFilters.MapFilters.MapRegion.id),
                ViewFilters.MapFilters.MapRegion.dropDownValues?.toList() ?: listOf()
            )
            it.shapedMapDropDown.setupDropDown(
                filter.getOrCreateField(ViewFilters.MapFilters.ShapedMap.id),
                ViewFilters.MapFilters.ShapedMap.dropDownValues?.toList() ?: listOf()
            )
            it.elderMapDropDown.setupDropDown(
                filter.getOrCreateField(ViewFilters.MapFilters.ElderMap.id),
                ViewFilters.MapFilters.ElderMap.dropDownValues?.toList() ?: listOf()
            )
            it.blightedMapDropDown.setupDropDown(
                filter.getOrCreateField(ViewFilters.MapFilters.BlightedMap.id),
                ViewFilters.MapFilters.BlightedMap.dropDownValues?.toList() ?: listOf()
            )
        }
    }

    override fun cleanFields() {
        viewBinding?.let {
            it.mapTierMinMax.cleanField()
            it.mapPacksizeMinMax.cleanField()
            it.mapIiqMinMax.cleanField()
            it.mapIirMinMax.cleanField()
            it.mapSeriesDropDown.cleanField()
            it.mapRegionDropDown.cleanField()
            it.shapedMapDropDown.cleanField()
            it.elderMapDropDown.cleanField()
            it.blightedMapDropDown.cleanField()
        }
    }

    override fun onViewRemoved(view: View?) {
        super.onViewRemoved(view)
        viewBinding = null
    }
}