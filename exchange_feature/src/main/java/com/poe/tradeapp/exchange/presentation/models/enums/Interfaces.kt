package com.poe.tradeapp.exchange.presentation.models.enums

import com.poe.tradeapp.exchange.presentation.models.Filter

internal interface IBindableFieldViewHolder {
    fun bind(item: IFilter, filter: Filter)
}

internal interface IBindableViewHolder {
    fun bind(item: ViewFilters.AllFilters, filters: MutableList<Filter>)
}

internal interface IEnum {
    val id: String?
    val text: String
}

internal interface IFilter : IEnum {
    val viewType: ViewType
    val dropDownValues: Array<*>?
}