package com.poetradeapp.listeners

import android.view.View
import android.widget.AdapterView
import com.poetradeapp.models.enums.IEnum
import com.poetradeapp.models.enums.IFilter
import com.poetradeapp.models.enums.ViewFilters
import com.poetradeapp.models.requestmodels.ItemRequestModelFields.DropDown
import com.poetradeapp.models.requestmodels.ItemRequestModelFields.Filters
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class DropDownChangedListener(
    private val item: IFilter,
    private val filters: Filters
) : AdapterView.OnItemClickListener {
    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val value = DropDown((p0?.getItemAtPosition(p2) as IEnum?)?.id)
        if (item is ViewFilters.TypeFilters) {
            val filter = filters.type_filters.filters
            when (item) {
                ViewFilters.TypeFilters.Category -> {
                    filter.category = value
                }
                ViewFilters.TypeFilters.Rarity -> {
                    filter.rarity = value
                }
            }
        }
        if (item is ViewFilters.MapFilters) {
            val filter = filters.map_filters.filters
            when (item) {
                ViewFilters.MapFilters.ShapedMap -> {
                    filter.mapShaped = value
                }
                ViewFilters.MapFilters.ElderMap -> {
                    filter.mapElder = value
                }
                ViewFilters.MapFilters.BlightedMap -> {
                    filter.mapBlighted = value
                }
                ViewFilters.MapFilters.MapRegion -> {
                    filter.mapRegion = value
                }
                ViewFilters.MapFilters.MapSeries -> {
                    filter.mapSeries = value
                }
            }
        }
        if (item is ViewFilters.MiscFilters) {
            val filter = filters.misc_filters.filters
            when (item) {
                ViewFilters.MiscFilters.GemQualityType -> {
                    filter.gem_alternate_quality = value
                }
                ViewFilters.MiscFilters.ShaperInfluence -> {
                    filter.shaper_item = value
                }
                ViewFilters.MiscFilters.ElderInfluence -> {
                    filter.elder_item = value
                }
                ViewFilters.MiscFilters.CrusaderInfluence -> {
                    filter.crusader_item = value
                }
                ViewFilters.MiscFilters.RedeemerInfluence -> {
                    filter.redeemer_item = value
                }
                ViewFilters.MiscFilters.HunterInfluence -> {
                    filter.hunter_item = value
                }
                ViewFilters.MiscFilters.WarlordInfluence -> {
                    filter.warlord_item = value
                }
                ViewFilters.MiscFilters.FracturedItem -> {
                    filter.fractured_item = value
                }
                ViewFilters.MiscFilters.SynthesisedItem -> {
                    filter.synthesised_item = value
                }
                ViewFilters.MiscFilters.AlternateArt -> {
                    filter.alternate_art = value
                }
                ViewFilters.MiscFilters.Identified -> {
                    filter.identified = value
                }
                ViewFilters.MiscFilters.Corrupted -> {
                    filter.corrupted = value
                }
                ViewFilters.MiscFilters.Mirrored -> {
                    filter.mirrored = value
                }
                ViewFilters.MiscFilters.Crafted -> {
                    filter.crafted = value
                }
                ViewFilters.MiscFilters.Veiled -> {
                    filter.veiled = value
                }
                ViewFilters.MiscFilters.Enchanted -> {
                    filter.enchanted = value
                }
            }
        }
        if (item is ViewFilters.TradeFilters) {
            val filter = filters.trade_filters.filters
            when (item) {
                ViewFilters.TradeFilters.Listed -> {
                    filter.indexed = value
                }
                ViewFilters.TradeFilters.SaleType -> {
                    filter.sale_type = value
                }
                ViewFilters.TradeFilters.BuyoutPrice -> {
                    filter.price.option = value.option
                }
            }
        }
    }
}