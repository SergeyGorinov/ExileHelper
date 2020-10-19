package com.poetradeapp.listeners

import android.text.Editable
import android.text.TextWatcher
import com.poetradeapp.models.EnumFilters
import com.poetradeapp.models.requestmodels.ItemRequestModelFields

class SocketsLinksTextChangedListener(
    private val item: EnumFilters.SocketFilters,
    private val type: EnumFilters.SocketTypes,
    private val filters: ItemRequestModelFields.SocketFilters
) : TextWatcher {

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        val value = p0?.toString()?.toIntOrNull()
        val filter = when (item) {
            EnumFilters.SocketFilters.Sockets -> filters.sockets
            EnumFilters.SocketFilters.Links -> filters.links
        }
        when (type) {
            EnumFilters.SocketTypes.R -> {
                filter.r = value
            }
            EnumFilters.SocketTypes.G -> {
                filter.g = value
            }
            EnumFilters.SocketTypes.B -> {
                filter.b = value
            }
            EnumFilters.SocketTypes.W -> {
                filter.w = value
            }
            EnumFilters.SocketTypes.MIN -> {
                filter.min = value
            }
            EnumFilters.SocketTypes.MAX -> {
                filter.max = value
            }
        }
    }

    override fun afterTextChanged(p0: Editable?) = Unit
}