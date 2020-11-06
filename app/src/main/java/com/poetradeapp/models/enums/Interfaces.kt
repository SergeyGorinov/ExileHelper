package com.poetradeapp.models.enums

interface IEnum {
    val id: String?
    val text: String
}

interface IFilter : IEnum {
    val viewType: ViewType
    val dropDownValues: Array<*>?
}