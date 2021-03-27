package com.poe.tradeapp.exchange.presentation

import androidx.lifecycle.ViewModel
import com.poe.tradeapp.core.domain.usecases.GetCurrencyItemsUseCase
import com.poe.tradeapp.core.domain.usecases.GetItemsUseCase
import com.poe.tradeapp.core.domain.usecases.GetStatsUseCase
import com.poe.tradeapp.exchange.data.models.ItemsRequestModel
import com.poe.tradeapp.exchange.data.models.ItemsRequestModelFields
import com.poe.tradeapp.exchange.domain.usecases.GetItemsDataListUseCase
import com.poe.tradeapp.exchange.domain.usecases.GetItemsListUseCase
import com.poe.tradeapp.exchange.presentation.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

internal class ItemsSearchViewModel(
    private val getCurrencyItemsUseCase: GetCurrencyItemsUseCase,
    private val getItemsUseCase: GetItemsUseCase,
    private val getStatsUseCase: GetStatsUseCase,
    private val getItemsListUseCase: GetItemsListUseCase,
    private val getItemsDataListUseCase: GetItemsDataListUseCase
) : ViewModel() {

    val viewLoadingState = MutableStateFlow(true)

    var itemGroups: List<ItemGroupViewData> = listOf()
        private set

    var statGroups: List<StatGroupViewData> = listOf()
        private set

    var currencies: List<CurrencyViewData> = listOf()
        private set

    var itemsResultData: Pair<String, List<String>>? = null
        private set

    val itemsResultFetchedData = mutableListOf<ItemResultViewData>()

    val filters = mutableListOf<Filter>()
    var type: String? = null
    var name: String? = null

    suspend fun requestItems() {
        withContext(Dispatchers.IO) {
            if (currencies.isEmpty()) {
                viewLoadingState.emit(true)
                currencies = getCurrencyItemsUseCase.execute().flatMap { it.items }.map {
                    CurrencyViewData(it.id, it.label, it.imageUrl)
                }
            }
            if (itemGroups.isEmpty()) {
                viewLoadingState.emit(true)
                itemGroups = getItemsUseCase.execute().map { group ->
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
            }
            if (statGroups.isEmpty()) {
                viewLoadingState.emit(true)
                statGroups = getStatsUseCase.execute().map { group ->
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
            }
        }
        viewLoadingState.emit(false)
    }

    suspend fun fetchPartialResults(league: String, position: Int) {
        withContext(Dispatchers.IO) {
            if (position == 0) {
                itemsResultFetchedData.clear()
                fetchAllEntries(league)
            }

            val subList = getResultsSubList(position)

            if (subList.isNotEmpty()) {
                val result = getItemsDataListUseCase.execute(
                    subList.joinToString(","),
                    itemsResultData?.first ?: ""
                ).map { itemData ->
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
                itemsResultFetchedData.addAll(result)
            }
        }
    }

    private suspend fun fetchAllEntries(league: String) {
        itemsResultData = getItemsListUseCase.execute(league, populateRequest())
    }

    private fun getResultsSubList(position: Int): List<String> {
        val itemsEntries = itemsResultData?.second ?: listOf()
        return if (itemsEntries.size - position > 10) {
            itemsEntries.subList(position, position + 10)
        } else
            itemsEntries.subList(position, position + (itemsEntries.size - position))
    }

    private fun populateRequest(): ItemsRequestModel {
        val requestModel = ItemsRequestModel()

        requestModel.query.name = name
        requestModel.query.type = type

        val reqFilters = mutableMapOf<String, ItemsRequestModelFields.Filter?>()

        filters.forEach { filter ->
            var reqFilter: ItemsRequestModelFields.Filter? = null
            if (filter.fields.any { a -> a.value != null }) {
                reqFilter = ItemsRequestModelFields.Filter(!filter.isEnabled)
                reqFilter.filters = ItemsRequestModelFields.FilterFields(filter.fields.map { m ->
                    ItemsRequestModelFields.FilterField(
                        m.name,
                        m.value
                    )
                })
            }
            reqFilters[filter.name] = reqFilter
        }

        requestModel.query.filters = ItemsRequestModelFields.Filters(reqFilters)

        return requestModel
    }
}