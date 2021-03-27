package com.poe.tradeapp.exchange.presentation.models.enums

internal interface IEnum {
    val id: String?
    val text: String
}

internal interface IFilter : IEnum {
    val viewType: ViewType
    val dropDownValues: Array<*>?
}