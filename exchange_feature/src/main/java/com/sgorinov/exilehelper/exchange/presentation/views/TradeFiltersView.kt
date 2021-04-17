package com.sgorinov.exilehelper.exchange.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.sgorinov.exilehelper.core.presentation.SlideUpDownAnimator
import com.sgorinov.exilehelper.exchange.R
import com.sgorinov.exilehelper.exchange.data.models.Filter
import com.sgorinov.exilehelper.exchange.databinding.TradeFiltersViewBinding
import com.sgorinov.exilehelper.exchange.presentation.models.enums.ViewFilters

internal class TradeFiltersView(ctx: Context, attrs: AttributeSet) :
    BaseExpandableView(ctx, attrs) {

    override val animator: SlideUpDownAnimator

    var viewBinding: TradeFiltersViewBinding?

    init {
        val view = LayoutInflater.from(ctx).inflate(R.layout.trade_filters_view, this, true)
        viewBinding = TradeFiltersViewBinding.bind(view)
        animator = SlideUpDownAnimator(view)
    }

    override fun setupFields(filter: Filter) {
        viewBinding?.let {
            it.account.setupField(
                filter.getOrCreateField(ViewFilters.TradeFilters.SellerAccount.id)
            )
            it.listedDropdown.setupDropDown(
                filter.getOrCreateField(ViewFilters.TradeFilters.Listed.id),
                ViewFilters.TradeFilters.Listed.dropDownValues?.toList() ?: listOf()
            )
            it.saleTypeDropdown.setupDropDown(
                filter.getOrCreateField(ViewFilters.TradeFilters.SaleType.id),
                ViewFilters.TradeFilters.SaleType.dropDownValues?.toList() ?: listOf()
            )
            it.buyoutDropdown.setupDropDown(
                filter.getOrCreateField(ViewFilters.TradeFilters.BuyoutPrice.id),
                ViewFilters.TradeFilters.BuyoutPrice.dropDownValues?.toList() ?: listOf()
            )
        }
    }

    override fun cleanFields() {
        viewBinding?.let {
            it.account.cleanField()
            it.listedDropdown.cleanField()
            it.saleTypeDropdown.cleanField()
            it.buyoutDropdown.cleanField()
        }
    }

    override fun onViewRemoved(view: View?) {
        super.onViewRemoved(view)
        viewBinding = null
    }
}