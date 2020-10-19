package com.poetradeapp.models.viewmodels

import androidx.lifecycle.ViewModel
import com.poetradeapp.models.ExchangeItemsResponseModel
import com.poetradeapp.models.requestmodels.ItemRequestModel

class ItemsSearchViewModel : ViewModel() {

    private val itemRequestData = ItemRequestModel()

    private var itemsResultData: List<ExchangeItemsResponseModel>? = null

    fun getItemRequestData() = itemRequestData

    fun setItemsResultData(itemsResultData: List<ExchangeItemsResponseModel>) {
        this.itemsResultData = itemsResultData
    }

    fun getItemsResultData() = itemsResultData ?: listOf()
}