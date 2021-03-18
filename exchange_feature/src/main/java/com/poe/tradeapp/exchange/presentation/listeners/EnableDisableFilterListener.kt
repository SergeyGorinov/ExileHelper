package com.poe.tradeapp.exchange.presentation.listeners

import android.widget.CompoundButton
import com.poe.tradeapp.exchange.data.models.ItemsRequestModelFields
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class EnableDisableFilterListener(private val filter: ItemsRequestModelFields.Filter?) :
    CompoundButton.OnCheckedChangeListener {

    override fun onCheckedChanged(p0: CompoundButton?, checked: Boolean) {
        filter?.disabled = !checked
    }
}