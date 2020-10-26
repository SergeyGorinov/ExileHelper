package com.poetradeapp.listeners

import android.widget.CompoundButton
import com.poetradeapp.models.requestmodels.Activatable
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class EnableDisableFilterListener(private val filter: Activatable) :
    CompoundButton.OnCheckedChangeListener {

    override fun onCheckedChanged(p0: CompoundButton?, checked: Boolean) {
        filter.disabled = !checked
    }
}