package com.poe.tradeapp.exchange.presentation

import androidx.lifecycle.ViewModel
import com.poe.tradeapp.core.domain.usecases.GetCurrencyItemsUseCase
import com.poe.tradeapp.core.domain.usecases.GetItemsUseCase
import com.poe.tradeapp.core.domain.usecases.GetStatsUseCase
import com.poe.tradeapp.exchange.data.models.ItemsListResponseModel
import com.poe.tradeapp.exchange.data.models.ItemsRequestModel
import com.poe.tradeapp.exchange.data.models.ItemsRequestModelFields
import com.poe.tradeapp.exchange.presentation.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

internal class ItemsSearchViewModel(
    private val getCurrencyItemsUseCase: GetCurrencyItemsUseCase,
    private val getItemsUseCase: GetItemsUseCase,
    private val getStatsUseCase: GetStatsUseCase,
) : ViewModel() {

    val viewLoadingState = MutableStateFlow(true)

    var itemGroups: List<ItemGroupViewData> = listOf()
        private set

    var statGroups: List<StatGroupViewData> = listOf()
        private set

    var currencies: List<CurrencyViewData> = listOf()
        private set

    var itemsResultData: ItemsListResponseModel? = null
        private set

    val filters = mutableListOf<Filter>()

    private var type: String? = null
    private var name: String? = null

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

    fun setType(type: String) {
        this.type = type
    }

    fun setName(name: String?) {
        this.name = name
    }

    suspend fun fetchPartialResults(position: Int) {

        if (itemsResultData == null || position == 0)
            fetchAllEntries()

        val subList = getResultsSubList(position)

//        if (subList.isNotEmpty()) {
//            featureRetrofit.getItemExchangeResponse(
//                subList.joinToString(","),
//                itemsResultData?.id ?: ""
//            ).await()
//        }
    }

    private suspend fun fetchAllEntries() {
//        itemsResultData = try {
//            featureRetrofit.getItemsExchangeList(
//                "api/trade/search/Heist", populateRequest()
//            ).await()
//        } catch (e: Exception) {
//            Log.e("ITEMS FETCHING ERROR", e.message ?: "ERROR")
//            null
//        }
    }

    private fun getResultsSubList(position: Int): List<String> {
        val itemsEntries = itemsResultData?.result ?: listOf()
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