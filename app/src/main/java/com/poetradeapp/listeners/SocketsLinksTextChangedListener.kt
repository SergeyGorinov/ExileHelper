package com.poetradeapp.listeners

import android.text.Editable
import android.text.TextWatcher
import com.poetradeapp.models.enums.ViewFilters
import com.poetradeapp.models.requestmodels.ItemRequestModelFields
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class SocketsLinksTextChangedListener(
    private val item: ViewFilters.SocketFilters,
    private val type: ViewFilters.SocketTypes,
    private val filters: ItemRequestModelFields.SocketFilters
) : TextWatcher {

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        val value = p0?.toString()?.toIntOrNull()
        val filter = when (item) {
            ViewFilters.SocketFilters.Sockets -> filters.sockets
            ViewFilters.SocketFilters.Links -> filters.links
        }
        when (type) {
            ViewFilters.SocketTypes.R -> {
                filter.r = value
            }
            ViewFilters.SocketTypes.G -> {
                filter.g = value
            }
            ViewFilters.SocketTypes.B -> {
                filter.b = value
            }
            ViewFilters.SocketTypes.W -> {
                filter.w = value
            }
            ViewFilters.SocketTypes.MIN -> {
                filter.min = value
            }
            ViewFilters.SocketTypes.MAX -> {
                filter.max = value
            }
        }
    }

    override fun afterTextChanged(p0: Editable?) = Unit
}