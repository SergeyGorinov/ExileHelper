package com.sgorinov.exilehelper.core.domain.usecases

import com.sgorinov.exilehelper.core.domain.ICoreRepository
import com.sgorinov.exilehelper.core.domain.models.Option
import com.sgorinov.exilehelper.core.domain.models.StatData
import com.sgorinov.exilehelper.core.domain.models.StatGroup

class GetStatsUseCase(private val repository: ICoreRepository) {

    fun execute(): List<StatGroup> {
        return repository.statData.map { group ->
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