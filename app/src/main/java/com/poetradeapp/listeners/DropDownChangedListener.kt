package com.poetradeapp.listeners

import android.view.View
import android.widget.AdapterView
import com.poetradeapp.models.EnumFilters
import com.poetradeapp.models.FiltersEnum
import com.poetradeapp.models.GenericEnum
import com.poetradeapp.models.requestmodels.ItemRequestModelFields.DropDown
import com.poetradeapp.models.requestmodels.ItemRequestModelFields.Filters

class DropDownChangedListener(
    private val item: FiltersEnum,
    private val filters: Filters
) : AdapterView.OnItemClickListener {
    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val value = DropDown((p0?.getItemAtPosition(p2) as GenericEnum?)?.id)
        if (item is EnumFilters.TypeFilters) {
            val filter = filters.type_filters.filters
            when (item) {
                EnumFilters.TypeFilters.Category -> {
                    filter.category = value
                }
                EnumFilters.TypeFilters.Rarity -> {
                    filter.rarity = value
                }
            }
        }
        if (item is EnumFilters.MapFilter) {
            val filter = filters.map_filters.filters
            when (item) {
                EnumFilters.MapFilter.ShapedMap -> {
                    filter.map_shaped = value
                }
                EnumFilters.MapFilter.ElderMap -> {
                    filter.map_elder = value
                }
                EnumFilters.MapFilter.BlightedMap -> {
                    filter.map_blighted = value
                }
                EnumFilters.MapFilter.MapRegion -> {
                    filter.map_region = value
                }
                EnumFilters.MapFilter.MapSeries -> {
                    filter.map_series = value
                }
            }
        }
        if (item is EnumFilters.MiscFilter) {
            val filter = filters.misc_filters.filters
            when (item) {
                EnumFilters.MiscFilter.GemQualityType -> {
                    filter.gem_alternate_quality = value
                }
                EnumFilters.MiscFilter.ShaperInfluence -> {
                    filter.shaper_item = value
                }
                EnumFilters.MiscFilter.ElderInfluence -> {
                    filter.elder_item = value
                }
                EnumFilters.MiscFilter.CrusaderInfluence -> {
                    filter.crusader_item = value
                }
                EnumFilters.MiscFilter.RedeemerInfluence -> {
                    filter.redeemer_item = value
                }
                EnumFilters.MiscFilter.HunterInfluence -> {
                    filter.hunter_item = value
                }
                EnumFilters.MiscFilter.WarlordInfluence -> {
                    filter.warlord_item = value
                }
                EnumFilters.MiscFilter.FracturedItem -> {
                    filter.fractured_item = value
                }
                EnumFilters.MiscFilter.SynthesisedItem -> {
                    filter.synthesised_item = value
                }
                EnumFilters.MiscFilter.AlternateArt -> {
                    filter.alternate_art = value
                }
                EnumFilters.MiscFilter.Identified -> {
                    filter.identified = value
                }
                EnumFilters.MiscFilter.Corrupted -> {
                    filter.corrupted = value
                }
                EnumFilters.MiscFilter.Mirrored -> {
                    filter.mirrored = value
                }
                EnumFilters.MiscFilter.Crafted -> {
                    filter.crafted = value
                }
                EnumFilters.MiscFilter.Veiled -> {
                    filter.veiled = value
                }
                EnumFilters.MiscFilter.Enchanted -> {
                    filter.enchanted = value
                }
            }
        }
        if (item is EnumFilters.TradeFilters) {
            val filter = filters.trade_filters.filters
            when (item) {
                EnumFilters.TradeFilters.Listed -> {
                    filter.indexed = value
                }
                EnumFilters.TradeFilters.SaleType -> {
                    filter.sale_type = value
                }
                EnumFilters.TradeFilters.BuyoutPrice -> {
                    filter.price.option = value.option
                }
            }
        }
    }
}