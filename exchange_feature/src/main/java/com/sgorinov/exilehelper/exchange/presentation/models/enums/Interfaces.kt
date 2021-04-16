package com.sgorinov.exilehelper.exchange.presentation.models.enums

import com.sgorinov.exilehelper.exchange.data.models.Filter

internal interface IBindableFieldViewHolder {
    fun bind(item: IFilter, filter: Filter)
}

internal interface IEnum {
    val id: String?
    val text: String
}

internal interface IFilter : IEnum {
    val viewType: ViewType
    val dropDownValues: Array<*>?
}