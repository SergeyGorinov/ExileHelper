package com.poe.tradeapp.exchange.presentation.adapters

import com.poe.tradeapp.exchange.presentation.models.Filter
import com.poe.tradeapp.exchange.presentation.models.enums.ViewFilters
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
internal interface IBindableViewHolder {
    fun bind(item: ViewFilters.AllFilters, filter: Filter)
}