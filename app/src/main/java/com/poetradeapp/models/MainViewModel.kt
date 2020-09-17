package com.poetradeapp.models

import android.widget.ImageButton
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private var currencyData: List<StaticEntries>? = null
    private var selectedButton: ImageButton? = null

    fun setMainData(data: List<StaticEntries>) {
        currencyData = data
    }

    fun getMainData() = currencyData

    fun setSelectedButton(button: ImageButton) {
        selectedButton?.isSelected = false
        button.isSelected = true
        selectedButton = button
    }
}