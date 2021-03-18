package com.poe.tradeapp.exchange.presentation.adapters

import com.poe.tradeapp.exchange.presentation.models.Filter
import com.poe.tradeapp.exchange.presentation.models.enums.IFilter
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
internal interface IBindableFieldViewHolder {
    fun bind(item: IFilter, filter: Filter)
}