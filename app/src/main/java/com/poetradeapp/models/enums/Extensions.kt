package com.poetradeapp.models.enums

import com.poetradeapp.models.requestmodels.Activatable
import com.poetradeapp.models.requestmodels.ItemRequestModelFields
import kotlinx.coroutines.ExperimentalCoroutinesApi

fun ViewFilters.AllFilters.getValuesByType(): Array<*> {
    return when (this) {
        ViewFilters.AllFilters.WeaponFilter -> ViewFilters.WeaponFilters.values()
        ViewFilters.AllFilters.ArmourFilter -> ViewFilters.ArmourFilters.values()
        ViewFilters.AllFilters.ReqFilter -> ViewFilters.ReqFilters.values()
        ViewFilters.AllFilters.MapFilter -> ViewFilters.MapFilters.values()
        ViewFilters.AllFilters.HeistFilter -> ViewFilters.HeistFilters.values()
        ViewFilters.AllFilters.MiscFilter -> ViewFilters.MiscFilters.values()
    }
}

@ExperimentalCoroutinesApi
fun ViewFilters.AllFilters.getFilterByType(filters: ItemRequestModelFields.Filters): Activatable =
    when (this) {
        ViewFilters.AllFilters.WeaponFilter -> filters.weapon_filters
        ViewFilters.AllFilters.ArmourFilter -> filters.armour_filters
        ViewFilters.AllFilters.ReqFilter -> filters.req_filters
        ViewFilters.AllFilters.MapFilter -> filters.map_filters
        ViewFilters.AllFilters.HeistFilter -> filters.heist_filters
        ViewFilters.AllFilters.MiscFilter -> filters.misc_filters
    }