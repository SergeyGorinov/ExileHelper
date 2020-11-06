package com.poetradeapp.listeners

import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputEditText
import com.poetradeapp.models.request.ItemsRequestModelFields.MinMax
import com.poetradeapp.models.ui.Field
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MinMaxTextChangedListener(
    private val filterMin: TextInputEditText,
    private val filterMax: TextInputEditText,
    private val field: Field
) : TextWatcher {

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        val min = filterMin.text?.toString()?.toIntOrNull()
        val max = filterMax.text?.toString()?.toIntOrNull()
        field.value = if (min == null && max == null) null else MinMax(min, max)
    }

    override fun afterTextChanged(p0: Editable?) = Unit
}