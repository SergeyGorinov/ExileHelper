package com.sgorinov.exilehelper.charts_feature.domain.usecases

import com.sgorinov.exilehelper.charts_feature.domain.IFeatureRepository
import com.sgorinov.exilehelper.charts_feature.domain.models.ItemGroup

internal class GetItemsGroupsUseCase(private val repository: IFeatureRepository) {

    fun execute(): List<ItemGroup> {
        return repository.getItemsGroups().map {
            ItemGroup(it.label, it.iconUrl, it.isCurrency, it.type)
        }
    }
}