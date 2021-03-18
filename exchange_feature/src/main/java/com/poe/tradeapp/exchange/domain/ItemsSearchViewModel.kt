package com.poe.tradeapp.exchange.domain

import android.util.Log
import androidx.lifecycle.ViewModel
import com.poe.tradeapp.core.DI
import com.poe.tradeapp.core.data.StaticApi
import com.poe.tradeapp.exchange.data.ApiService
import com.poe.tradeapp.exchange.data.models.ItemsListResponseModel
import com.poe.tradeapp.exchange.data.models.ItemsRequestModel
import com.poe.tradeapp.exchange.data.models.ItemsRequestModelFields
import com.poe.tradeapp.exchange.data.models.ResponseModel
import com.poe.tradeapp.exchange.presentation.models.Currency
import com.poe.tradeapp.exchange.presentation.models.Filter
import com.poe.tradeapp.exchange.presentation.models.enums.ViewState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.component.inject
import retrofit2.await

@ExperimentalCoroutinesApi
internal class ItemsSearchViewModel : ViewModel() {

    private val retrofit by DI.inject<StaticApi>()
    private val featureRetrofit by DI.inject<ApiService>()

    val loadingState = MutableStateFlow(ViewState.Loading)
    val responseItems = MutableStateFlow(listOf<ResponseModel>())

//    var items: List<ItemGroupWithItems> = listOf()
//        private set
//
//    var stats: List<StatGroupWithItems> = listOf()
//        private set

    var currency: List<Currency> = listOf()
        private set

    var itemsResultData: ItemsListResponseModel? = null
        private set

    private val filters = mutableListOf<Filter>()
    private var type: String? = null
    private var name: String? = null

//    suspend fun initData() {
//        items = repository.dao().getAllItems()
//        stats = repository.dao().getAllStats()
//        currency = repository.dao().getCurrencies().map { m -> Currency(m.id, m.label, m.image) }
//
//        if (items.isEmpty()) {
//            items = fetchItems()
//            items.forEach { group ->
//                repository.dao().insert(group)
//            }
//        }
//
//        if (stats.isEmpty()) {
//            stats = fetchStats()
//            stats.forEach { group ->
//                repository.dao().insert(group)
//            }
//        }
//
//        if (currency.isEmpty()) {
//            val staticData = fetchStaticData()
//            staticData.forEach { group ->
//                repository.dao().insert(group)
//            }
//            currency = staticData.single { s -> s.group.id == "Currency" }.items.map { m ->
//                Currency(
//                    m.id,
//                    m.label,
//                    m.image
//                )
//            }
//        }
//    }

    fun addFilter(filter: Filter) {
        filters.add(filter)
    }

    fun getFilter(name: String) = filters.singleOrNull { s -> s.name == name }

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

        if (subList.isNotEmpty()) {
            try {
                val baseFetchUrl = StringBuilder("/api/trade/fetch/")
                baseFetchUrl.append(subList.joinToString(","))
                baseFetchUrl.append("?query=${itemsResultData?.id}")
                responseItems.emit(
                    featureRetrofit.getItemExchangeResponse(baseFetchUrl.toString()).await().result
                )
            } catch (e: Exception) {
                Log.e("ITEMS FETCHING ERROR", e.stackTraceToString())
            }
        }
    }

    private suspend fun fetchAllEntries() {
        itemsResultData = try {
            featureRetrofit.getItemsExchangeList(
                "api/trade/search/Heist", populateRequest()
            ).await()
        } catch (e: Exception) {
            Log.e("ITEMS FETCHING ERROR", e.message ?: "ERROR")
            null
        }
    }

//    private suspend fun fetchItems(): List<ItemGroupWithItems> {
//        return try {
//            val groups = mutableListOf<ItemGroupWithItems>()
//            retrofit.getItemsData("api/trade/data/items").await().result.forEach { group ->
//                val items = mutableListOf<ItemModel>()
//                group.entries.forEach { item ->
//                    items.add(
//                        ItemModel(
//                            type = item.type,
//                            text = item.text,
//                            name = item.name,
//                            disc = item.disc,
//                            unique = item.flags?.unique,
//                            prophecy = item.flags?.prophecy
//                        )
//                    )
//                }
//                groups.add(
//                    ItemGroupWithItems(
//                        ItemGroupModel(label = group.label),
//                        items
//                    )
//                )
//            }
//            groups
//        } catch (e: Exception) {
//            Log.e("ITEMS_FETCHING_ERROR", e.message ?: "ERROR")
//            listOf()
//        }
//    }
//
//    private suspend fun fetchStats(): List<StatGroupWithItems> {
//        return try {
//            retrofit.getStatsData("api/trade/data/stats").await().result.map { m ->
//                StatGroupWithItems(
//                    StatGroupModel(label = m.label),
//                    m.entries.map { item ->
//                        StatModel(
//                            id = item.id,
//                            text = item.text,
//                            type = item.type,
//                            options = item.option?.options?.map { option ->
//                                Option(
//                                    option.id,
//                                    option.text
//                                )
//                            }
//                        )
//                    }
//                )
//            }
//        } catch (e: java.lang.Exception) {
//            Log.e("STATS_FETCHING_ERROR", e.message ?: "ERROR")
//            listOf()
//        }
//    }
//
//    private suspend fun fetchStaticData(): List<StaticGroupWithItems> {
//        return try {
//            retrofit.getStaticData("api/trade/data/static").await().result.map { m ->
//                StaticGroupWithItems(
//                    StaticGroupModel(id = m.id, label = m.label),
//                    m.entries.map { item ->
//                        StaticItemModel(
//                            id = item.id,
//                            label = item.text,
//                            image = item.image
//                        )
//                    }
//                )
//            }
//        } catch (e: java.lang.Exception) {
//            Log.e("STATS_FETCHING_ERROR", e.message ?: "ERROR")
//            listOf()
//        }
//    }

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