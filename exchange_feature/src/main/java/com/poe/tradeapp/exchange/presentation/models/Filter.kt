package com.poe.tradeapp.exchange.presentation.models

import kotlin.properties.Delegates

class Filter(val name: String, private val onFieldsChanged: (Boolean) -> Unit) {

    var isEnabled: Boolean = false
    val fields: MutableList<Field> = mutableListOf()

    private val isFieldsEmpty
        get() = fields.isEmpty() || fields.all { it.value == null }

    fun getField(id: String): Field {
        var field = fields.singleOrNull { s -> s.name == id }
        if (field == null) {
            field = Field(id) {
                onFieldsChanged(isFieldsEmpty)
            }
            addField(field)
        }
        return field
    }

    fun cleanFilter() {
        fields.clear()
        onFieldsChanged(isFieldsEmpty)
    }

    private fun addField(field: Field) {
        fields.add(field)
        onFieldsChanged(isFieldsEmpty)
    }
}

data class Field(
    val name: String,
    val onValueChange: () -> Unit
) {
    var value by Delegates.observable<Any?>(null) { _, _, _ ->
        onValueChange()
    }
}