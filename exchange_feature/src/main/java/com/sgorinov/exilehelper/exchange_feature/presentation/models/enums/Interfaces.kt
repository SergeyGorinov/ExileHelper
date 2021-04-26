package com.sgorinov.exilehelper.exchange_feature.presentation.models.enums

import com.sgorinov.exilehelper.exchange_feature.data.models.LocalFilter

internal interface IBindableFieldViewHolder {
    fun bind(item: IFilter, localFilter: LocalFilter)
}

internal interface IEnum {
    val id: String?
    val text: String
}

internal interface IFilter : IEnum {
    val viewType: ViewType
    val dropDownValues: List<IEnum>?
}