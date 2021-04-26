package com.sgorinov.exilehelper.exchange_feature.domain.usecases

import com.sgorinov.exilehelper.exchange_feature.domain.IFeatureRepository

internal class SetItemDataUseCase(private val repository: IFeatureRepository) {

    fun execute(type: String?, name: String?) {
        repository.setItemData(type, name)
    }
}