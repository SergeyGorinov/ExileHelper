package com.poe.tradeapp.core.domain.usecases

import com.poe.tradeapp.core.domain.ICoreRepository
import com.poe.tradeapp.core.domain.models.ItemData
import com.poe.tradeapp.core.domain.models.ItemFlag
import com.poe.tradeapp.core.domain.models.ItemGroup

class GetItemsUseCase(private val repository: ICoreRepository) {

    suspend fun execute(): List<ItemGroup> {
        return repository.itemData.map { group ->
            val entries = group.entries.map {
                ItemData(
                    it.type,
                    it.text,
                    it.name,
                    it.disc,
                    ItemFlag(it.flags?.unique, it.flags?.prophecy)
                )
            }
            ItemGroup(group.label, entries)
        }
    }
}