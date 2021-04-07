package com.poe.tradeapp.exchange.domain.usecases

import com.poe.tradeapp.exchange.domain.IFeatureRepository

internal class SetItemDataUseCase(private val repository: IFeatureRepository) {

    fun execute(type: String, name: String?) {
        repository.setItemData(type, name)
    }
}