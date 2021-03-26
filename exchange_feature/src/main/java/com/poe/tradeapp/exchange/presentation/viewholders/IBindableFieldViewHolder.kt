package com.poe.tradeapp.exchange.presentation.viewholders

import com.poe.tradeapp.exchange.presentation.models.Filter
import com.poe.tradeapp.exchange.presentation.models.enums.IFilter

internal interface IBindableFieldViewHolder {
    fun bind(item: IFilter, filter: Filter)
}