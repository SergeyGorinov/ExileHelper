package com.sgorinov.exilehelper.exchange.domain.usecases

import com.sgorinov.exilehelper.exchange.domain.IFeatureRepository

internal class SetItemDataUseCase(private val repository: IFeatureRepository) {

    fun execute(type: String?, name: String?) {
        repository.setItemData(type, name)
    }
}