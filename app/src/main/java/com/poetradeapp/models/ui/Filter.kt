package com.poetradeapp.models.ui

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

@ExperimentalCoroutinesApi
class Filter(val name: String) {
    val isFilterEmpty = MutableStateFlow(true)
    var isEnabled: Boolean = false

    val fields: MutableList<Field> = mutableListOf()

    fun getField(id: String): Field {
        var field = fields.singleOrNull { s -> s.name == id }
        if (field == null) {
            field = Field(id)
            fields.add(field)
            GlobalScope.launch {
                field.isFieldEmpty.collect {
                    isFilterEmpty.value = isEmpty()
                }
            }
        }
        return field
    }

    fun cleanFiler() {
        fields.forEach {
            it.value = null
        }
    }

    private fun isEmpty(): Boolean {
        return fields.all { a -> a.value == null }
    }
}

@ExperimentalCoroutinesApi
class Field(val name: String) {
    val isFieldEmpty: MutableStateFlow<Boolean> = MutableStateFlow(true)

    var value: Any? by Delegates.observable(null) { _, _, new ->
        isFieldEmpty.value = new == null
    }
}