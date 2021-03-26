package com.poe.tradeapp.exchange.presentation.viewholders

import com.poe.tradeapp.exchange.presentation.models.Filter
import com.poe.tradeapp.exchange.presentation.models.enums.ViewFilters

internal interface IBindableViewHolder {
    fun bind(item: ViewFilters.AllFilters, filters: MutableList<Filter>)
}