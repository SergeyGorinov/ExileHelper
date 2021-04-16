package com.sgorinov.exilehelper.core.domain.usecases

import com.sgorinov.exilehelper.core.domain.ICoreRepository
import com.sgorinov.exilehelper.core.domain.models.ItemData
import com.sgorinov.exilehelper.core.domain.models.ItemFlag
import com.sgorinov.exilehelper.core.domain.models.ItemGroup

class GetItemsUseCase(private val repository: ICoreRepository) {

    fun execute(): List<ItemGroup> {
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