package com.sgorinov.exilehelper.exchange.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.sgorinov.exilehelper.core.presentation.SlideUpDownAnimator
import com.sgorinov.exilehelper.exchange.R
import com.sgorinov.exilehelper.exchange.data.models.Filter
import com.sgorinov.exilehelper.exchange.databinding.BuyoutFilterViewBinding
import com.sgorinov.exilehelper.exchange.presentation.models.enums.ViewFilters

internal class BuyoutFilterView(ctx: Context, attrs: AttributeSet) :
    ConstraintLayout(ctx, attrs) {

    val animator: SlideUpDownAnimator
    val viewBinding: BuyoutFilterViewBinding

    init {
        val view = LayoutInflater.from(ctx).inflate(R.layout.buyout_filter_view, this, true)
        animator = SlideUpDownAnimator(view)
        viewBinding = BuyoutFilterViewBinding.bind(view)
    }

    fun setupFilters(filters: MutableList<Filter>) {
        val filter = if (filters.any { it.name == ViewFilters.AllFilters.TradeFilter.id }) {
            filters.first { it.name == ViewFilters.AllFilters.TradeFilter.id }
        } else {
            val newFilter = Filter(ViewFilters.AllFilters.TradeFilter.id) {
                //TODO
            }
            filters.add(newFilter)
            newFilter
        }
        viewBinding.listedDropdown.setupDropDown(
            filter.getField(ViewFilters.TradeFilters.Listed.id),
            ViewFilters.TradeFilters.Listed.dropDownValues?.toList() ?: listOf()
        )
        viewBinding.saleTypeDropdown.setupDropDown(
            filter.getField(ViewFilters.TradeFilters.SaleType.id),
            ViewFilters.TradeFilters.SaleType.dropDownValues?.toList() ?: listOf()
        )
        viewBinding.buyoutDropdown.setupDropDown(
            filter.getField(ViewFilters.TradeFilters.BuyoutPrice.id),
            ViewFilters.TradeFilters.BuyoutPrice.dropDownValues?.toList() ?: listOf()
        )
    }
}