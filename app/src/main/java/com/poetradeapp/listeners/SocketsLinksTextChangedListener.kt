package com.poetradeapp.listeners

import android.text.Editable
import android.text.TextWatcher
import com.poetradeapp.models.request.ItemsRequestModelFields
import com.poetradeapp.models.ui.Field
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class SocketsLinksTextChangedListener(
    private val data: Map<String, Editable?>,
    private val field: Field
) : TextWatcher {

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        field.value =
            if (data.all { a -> a.value == null }) null
            else ItemsRequestModelFields.Sockets(
                data["r"]?.toString()?.toIntOrNull(),
                data["g"]?.toString()?.toIntOrNull(),
                data["b"]?.toString()?.toIntOrNull(),
                data["w"]?.toString()?.toIntOrNull(),
                data["min"]?.toString()?.toIntOrNull(),
                data["max"]?.toString()?.toIntOrNull()
            )
    }

    override fun afterTextChanged(p0: Editable?) = Unit
}