package com.poe.tradeapp.core.domain.usecases

import com.poe.tradeapp.core.domain.ICoreRepository
import com.poe.tradeapp.core.domain.models.Option
import com.poe.tradeapp.core.domain.models.StatData
import com.poe.tradeapp.core.domain.models.StatGroup

class GetStatsUseCase(private val repository: ICoreRepository) {

    suspend fun execute(): List<StatGroup> {
        return repository.getStats().map { group ->
            val entries =
                group.entries.map { entry ->
                    StatData(
                        entry.id,
                        entry.text,
                        entry.type,
                        entry.option?.options?.map { Option(it.id, it.text) })
                }
            StatGroup(group.label, entries)
        }
    }
}