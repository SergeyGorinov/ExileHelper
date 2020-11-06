package com.poetradeapp.adapters

import com.poetradeapp.models.enums.IFilter
import com.poetradeapp.models.enums.ViewFilters
import com.poetradeapp.models.ui.Filter
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
interface IBindableFieldViewHolder {
    fun bind(item: IFilter, filter: Filter)
}

@ExperimentalCoroutinesApi
interface IBindableViewHolder {
    fun bind(item: ViewFilters.AllFilters, filter: Filter)
}