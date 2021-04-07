package com.poe.tradeapp.exchange.presentation

import androidx.lifecycle.ViewModel
import com.poe.tradeapp.core.domain.usecases.GetCurrencyItemsUseCase
import com.poe.tradeapp.core.domain.usecases.GetItemsUseCase
import com.poe.tradeapp.core.domain.usecases.GetStatsUseCase
import com.poe.tradeapp.exchange.domain.usecases.GetFiltersUseCase
import com.poe.tradeapp.exchange.domain.usecases.GetItemsResultDataUseCase
import com.poe.tradeapp.exchange.domain.usecases.GetTotalItemsResultCountUseCase
import com.poe.tradeapp.exchange.domain.usecases.SetItemDataUseCase
import com.poe.tradeapp.exchange.presentation.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

internal class ItemsSearchViewModel(
    getCurrencyItemsUseCase: GetCurrencyItemsUseCase,
    getItemsUseCase: GetItemsUseCase,
    getStatsUseCase: GetStatsUseCase,
    getFiltersUseCase: GetFiltersUseCase,
    getTotalItemsResultCountUseCase: GetTotalItemsResultCountUseCase,
    private val getItemsResultDataUseCase: GetItemsResultDataUseCase,
    private val setItemDataUseCase: SetItemDataUseCase
) : ViewModel() {

    val viewLoadingState = MutableStateFlow(false)

    val itemGroups = getItemsUseCase.execute().map { group ->
        val entries = group.entries.map {
            ItemViewData(
                it.type,
                it.text,
                it.name,
                it.disc,
                Flag(it.flags?.unique, it.flags?.prophecy)
            )
        }
        ItemGroupViewData(group.groupName, entries)
    }

    val statGroups = getStatsUseCase.execute().map { group ->
        val entries = group.entries.map { item ->
            StatViewData(
                item.id,
                item.text,
                item.type,
                item.options?.map { Option(it.id, it.text) }
            )
        }
        StatGroupViewData(group.groupName, entries)
    }

    val currencies = getCurrencyItemsUseCase.execute().flatMap { it.items }.map {
        CurrencyViewData(it.id, it.label, it.imageUrl)
    }

    val filters = getFiltersUseCase.execute()

    val totalItemsCount = getTotalItemsResultCountUseCase.execute()

    suspend fun fetchPartialResults(league: String, position: Int): List<ItemResultViewData> {
        return withContext(Dispatchers.IO) {
            getItemsResultDataUseCase.execute(league, position).map { itemData ->
                val hybridData = if (itemData.hybridData != null) {
                    HybridData(
                        itemData.hybridData.isVaalGem,
                        itemData.hybridData.properties.map { property ->
                            val values = property.values.map {
                                PropertyData(
                                    it.propertyValue,
                                    it.propertyColor
                                )
                            }
                            Property(
                                property.name,
                                values,
                                property.displayMode,
                                property.progress,
                                property.type,
                                property.suffix
                            )
                        },
                        itemData.hybridData.requirements.map { property ->
                            val values = property.values.map {
                                PropertyData(
                                    it.propertyValue,
                                    it.propertyColor
                                )
                            }
                            Property(
                                property.name,
                                values,
                                property.displayMode,
                                property.progress,
                                property.type,
                                property.suffix
                            )
                        },
                        itemData.hybridData.secDescrText,
                        itemData.hybridData.implicitMods,
                        itemData.hybridData.explicitMods,
                        null
                    )
                } else {
                    null
                }
                ItemResultViewData(
                    itemData.name,
                    itemData.typeLine,
                    itemData.iconUrl,
                    itemData.frameType,
                    itemData.synthesised,
                    itemData.replica,
                    itemData.corrupted,
                    itemData.sockets?.map { Socket(it.group, it.attr, it.sColour) },
                    itemData.hybridTypeLine,
                    Influences(
                        itemData.influences.elder,
                        itemData.influences.shaper,
                        itemData.influences.warlord,
                        itemData.influences.hunter,
                        itemData.influences.redeemer,
                        itemData.influences.crusader
                    ),
                    ItemData(
                        itemData.itemData.properties.map { property ->
                            val values = property.values.map {
                                PropertyData(
                                    it.propertyValue,
                                    it.propertyColor
                                )
                            }
                            Property(
                                property.name,
                                values,
                                property.displayMode,
                                property.progress,
                                property.type,
                                property.suffix
                            )
                        },
                        itemData.itemData.requirements.map { property ->
                            val values = property.values.map {
                                PropertyData(
                                    it.propertyValue,
                                    it.propertyColor
                                )
                            }
                            Property(
                                property.name,
                                values,
                                property.displayMode,
                                property.progress,
                                property.type,
                                property.suffix
                            )
                        },
                        itemData.itemData.secDescrText,
                        itemData.itemData.implicitMods,
                        itemData.itemData.explicitMods,
                        itemData.itemData.note
                    ),
                    hybridData
                )
            }
        }
    }

    fun setItemData(type: String, name: String?) {
        setItemDataUseCase.execute(type, name)
    }
}