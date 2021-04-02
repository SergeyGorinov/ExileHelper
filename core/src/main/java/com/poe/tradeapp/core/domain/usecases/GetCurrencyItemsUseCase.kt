package com.poe.tradeapp.core.domain.usecases

import com.poe.tradeapp.core.domain.ICoreRepository
import com.poe.tradeapp.core.domain.models.CurrencyGroup
import com.poe.tradeapp.core.domain.models.CurrencyItem

class GetCurrencyItemsUseCase(private val repository: ICoreRepository) {

    fun execute(): List<CurrencyGroup> {
        return repository.staticData.map { group ->
            val items = group.entries.map {
                val imageUrl = if (it.image != null) {
                    "https://www.pathofexile.com${it.image}"
                } else {
                    null
                }
                CurrencyItem(it.id, it.text, imageUrl)
            }
            CurrencyGroup(group.id, group.label, items)
        }
    }
}