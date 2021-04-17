package com.sgorinov.exilehelper.exchange.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.sgorinov.exilehelper.core.presentation.SlideUpDownAnimator
import com.sgorinov.exilehelper.exchange.R
import com.sgorinov.exilehelper.exchange.data.models.Filter
import com.sgorinov.exilehelper.exchange.databinding.MiscFiltersViewBinding
import com.sgorinov.exilehelper.exchange.presentation.models.enums.ViewFilters

internal class MiscFiltersView(ctx: Context, attrs: AttributeSet) : BaseExpandableView(ctx, attrs) {

    override val animator: SlideUpDownAnimator

    private var viewBinding: MiscFiltersViewBinding?

    init {
        val view = LayoutInflater.from(ctx).inflate(R.layout.misc_filters_view, this, true)
        viewBinding = MiscFiltersViewBinding.bind(view)
        animator = SlideUpDownAnimator(view)
    }

    override fun setupFields(filter: Filter) {
        viewBinding?.let {
            it.itemLevel.setupMinMax(
                filter.getOrCreateField(ViewFilters.MiscFilters.ItemLevel.id)
            )
            it.gemQualityType.setupDropDown(
                filter.getOrCreateField(ViewFilters.MiscFilters.GemQualityType.id),
                ViewFilters.MiscFilters.GemQualityType.dropDownValues?.toList() ?: listOf()
            )
            it.quality.setupMinMax(
                filter.getOrCreateField(ViewFilters.MiscFilters.Quality.id)
            )
            it.gemLevel.setupMinMax(
                filter.getOrCreateField(ViewFilters.MiscFilters.GemLevel.id)
            )
            it.gemExperience.setupMinMax(
                filter.getOrCreateField(ViewFilters.MiscFilters.GemExperience.id)
            )
            it.shaperInfluence.setupDropDown(
                filter.getOrCreateField(ViewFilters.MiscFilters.ItemLevel.id),
                ViewFilters.MiscFilters.ShaperInfluence.dropDownValues?.toList() ?: listOf()
            )
            it.elderInfluence.setupDropDown(
                filter.getOrCreateField(ViewFilters.MiscFilters.ElderInfluence.id),
                ViewFilters.MiscFilters.ElderInfluence.dropDownValues?.toList() ?: listOf()
            )
            it.crusaderInfluence.setupDropDown(
                filter.getOrCreateField(ViewFilters.MiscFilters.CrusaderInfluence.id),
                ViewFilters.MiscFilters.CrusaderInfluence.dropDownValues?.toList() ?: listOf()
            )
            it.redeemerInfluence.setupDropDown(
                filter.getOrCreateField(ViewFilters.MiscFilters.RedeemerInfluence.id),
                ViewFilters.MiscFilters.RedeemerInfluence.dropDownValues?.toList() ?: listOf()
            )
            it.hunterInfluence.setupDropDown(
                filter.getOrCreateField(ViewFilters.MiscFilters.HunterInfluence.id),
                ViewFilters.MiscFilters.HunterInfluence.dropDownValues?.toList() ?: listOf()
            )
            it.warlordInfluence.setupDropDown(
                filter.getOrCreateField(ViewFilters.MiscFilters.WarlordInfluence.id),
                ViewFilters.MiscFilters.WarlordInfluence.dropDownValues?.toList() ?: listOf()
            )
            it.fracturedItem.setupDropDown(
                filter.getOrCreateField(ViewFilters.MiscFilters.FracturedItem.id),
                ViewFilters.MiscFilters.FracturedItem.dropDownValues?.toList() ?: listOf()
            )
            it.synthesisedItem.setupDropDown(
                filter.getOrCreateField(ViewFilters.MiscFilters.SynthesisedItem.id),
                ViewFilters.MiscFilters.SynthesisedItem.dropDownValues?.toList() ?: listOf()
            )
            it.alternateArt.setupDropDown(
                filter.getOrCreateField(ViewFilters.MiscFilters.AlternateArt.id),
                ViewFilters.MiscFilters.AlternateArt.dropDownValues?.toList() ?: listOf()
            )
            it.identified.setupDropDown(
                filter.getOrCreateField(ViewFilters.MiscFilters.Identified.id),
                ViewFilters.MiscFilters.Identified.dropDownValues?.toList() ?: listOf()
            )
            it.corrupted.setupDropDown(
                filter.getOrCreateField(ViewFilters.MiscFilters.Corrupted.id),
                ViewFilters.MiscFilters.Corrupted.dropDownValues?.toList() ?: listOf()
            )
            it.mirrored.setupDropDown(
                filter.getOrCreateField(ViewFilters.MiscFilters.Mirrored.id),
                ViewFilters.MiscFilters.Mirrored.dropDownValues?.toList() ?: listOf()
            )
            it.crafted.setupDropDown(
                filter.getOrCreateField(ViewFilters.MiscFilters.Crafted.id),
                ViewFilters.MiscFilters.Crafted.dropDownValues?.toList() ?: listOf()
            )
            it.veiled.setupDropDown(
                filter.getOrCreateField(ViewFilters.MiscFilters.Veiled.id),
                ViewFilters.MiscFilters.Veiled.dropDownValues?.toList() ?: listOf()
            )
            it.enchanted.setupDropDown(
                filter.getOrCreateField(ViewFilters.MiscFilters.Enchanted.id),
                ViewFilters.MiscFilters.Enchanted.dropDownValues?.toList() ?: listOf()
            )
            it.talismanTier.setupMinMax(
                filter.getOrCreateField(ViewFilters.MiscFilters.TalismanTier.id)
            )
            it.storedExperience.setupMinMax(
                filter.getOrCreateField(ViewFilters.MiscFilters.StoredExperience.id)
            )
            it.stackSize.setupMinMax(
                filter.getOrCreateField(ViewFilters.MiscFilters.StackSize.id)
            )
        }
    }

    override fun cleanFields() {
        viewBinding?.let {
            it.itemLevel.cleanField()
            it.gemQualityType.cleanField()
            it.quality.cleanField()
            it.gemLevel.cleanField()
            it.gemExperience.cleanField()
            it.shaperInfluence.cleanField()
            it.elderInfluence.cleanField()
            it.crusaderInfluence.cleanField()
            it.redeemerInfluence.cleanField()
            it.hunterInfluence.cleanField()
            it.warlordInfluence.cleanField()
            it.fracturedItem.cleanField()
            it.synthesisedItem.cleanField()
            it.alternateArt.cleanField()
            it.identified.cleanField()
            it.corrupted.cleanField()
            it.mirrored.cleanField()
            it.crafted.cleanField()
            it.veiled.cleanField()
            it.enchanted.cleanField()
            it.talismanTier.cleanField()
            it.storedExperience.cleanField()
            it.stackSize.cleanField()
        }
    }

    override fun onViewRemoved(view: View?) {
        super.onViewRemoved(view)
        viewBinding = null
    }

}