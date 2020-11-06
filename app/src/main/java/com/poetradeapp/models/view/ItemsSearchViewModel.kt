package com.poetradeapp.models.view

import android.content.Context
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.util.Log
import androidx.lifecycle.ViewModel
import com.poetradeapp.R
import com.poetradeapp.dl.DatabaseRepository
import com.poetradeapp.http.RequestService
import com.poetradeapp.models.database.*
import com.poetradeapp.models.enums.ViewState
import com.poetradeapp.models.request.ItemsRequestModel
import com.poetradeapp.models.request.ItemsRequestModelFields
import com.poetradeapp.models.response.ExchangeItemsResponseModel
import com.poetradeapp.models.response.ItemsListResponseModel
import com.poetradeapp.models.ui.Currency
import com.poetradeapp.models.ui.FetchedItem
import com.poetradeapp.models.ui.Filter
import com.poetradeapp.ui.CenteredImageSpan
import com.poetradeapp.utility.Helpers.Companion.getInfluenceIcons
import com.poetradeapp.utility.Helpers.Companion.getSeparator
import com.poetradeapp.utility.Helpers.Companion.getSpannableTextGroup
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.await

@ExperimentalCoroutinesApi
class ItemsSearchViewModel(
    private val repository: DatabaseRepository,
    private val retrofit: RequestService,
    private val context: Context
) : ViewModel() {

    val loadingState = MutableStateFlow(ViewState.Loading)

    var items: List<ItemGroupWithItems> = listOf()
        private set

    var stats: List<StatGroupWithItems> = listOf()
        private set

    var currency: List<Currency> = listOf()
        private set

    var itemsResultData: ItemsListResponseModel? = null
        private set

    var fetchedItems: List<FetchedItem> = listOf()
        private set

    private val filters = mutableListOf<Filter>()
    private var type: String? = null
    private var name: String? = null

    suspend fun initData() {
        items = repository.dao().getAllItems()
        stats = repository.dao().getAllStats()
        currency = repository.dao().getCurrencies().map { m -> Currency(m.id, m.label, m.image) }

        if (items.isEmpty()) {
            items = fetchItems()
            items.forEach { group ->
                repository.dao().insert(group)
            }
        }

        if (stats.isEmpty()) {
            stats = fetchStats()
            stats.forEach { group ->
                repository.dao().insert(group)
            }
        }

        if (currency.isEmpty()) {
            val staticData = fetchStaticData()
            staticData.forEach { group ->
                repository.dao().insert(group)
            }
            currency = staticData.single { s -> s.group.id == "Currency" }.items.map { m ->
                Currency(
                    m.id,
                    m.label,
                    m.image
                )
            }
        }
    }

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

        fetchedItems = if (subList.isNotEmpty()) {
            try {
                val baseFetchUrl = StringBuilder("/api/trade/fetch/")
                baseFetchUrl.append(subList.joinToString(","))
                baseFetchUrl.append("?query=${itemsResultData?.id}")
                populateResponse(
                    retrofit.getItemExchangeResponse(baseFetchUrl.toString()).await().result
                )
            } catch (e: Exception) {
                Log.e("ITEMS FETCHING ERROR", e.stackTraceToString())
                listOf()
            }
        } else
            listOf()
    }

    private suspend fun fetchAllEntries() {
        itemsResultData = try {
            retrofit.getItemsExchangeList(
                "api/trade/search/Heist", populateRequest()
            ).await()
        } catch (e: Exception) {
            Log.e("ALL ITEMS FETCHING ERROR", e.message ?: "ERROR")
            null
        }
    }

    private suspend fun fetchItems(): List<ItemGroupWithItems> {
        return try {
            val groups = mutableListOf<ItemGroupWithItems>()
            retrofit.getItemsData("api/trade/data/items").await().result.forEach { group ->
                val items = mutableListOf<ItemModel>()
                group.entries.forEach { item ->
                    items.add(
                        ItemModel(
                            type = item.type,
                            text = item.text,
                            name = item.name,
                            disc = item.disc,
                            unique = item.flags?.unique,
                            prophecy = item.flags?.prophecy
                        )
                    )
                }
                groups.add(
                    ItemGroupWithItems(
                        ItemGroupModel(label = group.label),
                        items
                    )
                )
            }
            groups
        } catch (e: Exception) {
            Log.e("ITEMS_FETCHING_ERROR", e.message ?: "ERROR")
            listOf()
        }
    }

    private suspend fun fetchStats(): List<StatGroupWithItems> {
        return try {
            retrofit.getStatsData("api/trade/data/stats").await().result.map { m ->
                StatGroupWithItems(
                    StatGroupModel(label = m.label),
                    m.entries.map { item ->
                        StatModel(
                            id = item.id,
                            text = item.text,
                            type = item.type,
                            options = item.option?.options?.map { option ->
                                Option(
                                    option.id,
                                    option.text
                                )
                            }
                        )
                    }
                )
            }
        } catch (e: java.lang.Exception) {
            Log.e("STATS_FETCHING_ERROR", e.message ?: "ERROR")
            listOf()
        }
    }

    private suspend fun fetchStaticData(): List<StaticGroupWithItems> {
        return try {
            retrofit.getStaticData("api/trade/data/static").await().result.map { m ->
                StaticGroupWithItems(
                    StaticGroupModel(id = m.id, label = m.label),
                    m.entries.map { item ->
                        StaticItemModel(
                            id = item.id,
                            label = item.text,
                            image = item.image
                        )
                    }
                )
            }
        } catch (e: java.lang.Exception) {
            Log.e("STATS_FETCHING_ERROR", e.message ?: "ERROR")
            listOf()
        }
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

    private fun populateResponse(items: List<ExchangeItemsResponseModel>): List<FetchedItem> {
        var isOddRow = true
        return items.map { item ->
            var hybridItemText: SpannableStringBuilder? = null

            val currentItem = item.item
            val itemText = SpannableStringBuilder()
            val colorId =
                if (isOddRow) R.color.odd_result_row_color
                else R.color.even_result_row_color

            isOddRow = !isOddRow

            listOfNotNull(
                getSpannableTextGroup(currentItem.properties, "\n"),
                getSpannableTextGroup(currentItem.requirements, ", "),
                currentItem.secDescrText,
                currentItem.implicitMods,
                currentItem.explicitMods,
                currentItem.note
            ).forEach {
                populateItemText(it, itemText, currentItem.frameType)
            }

            if (currentItem.hybrid != null) {
                hybridItemText = SpannableStringBuilder()

                listOfNotNull(
                    getSpannableTextGroup(currentItem.hybrid.properties, "\n"),
                    getSpannableTextGroup(currentItem.hybrid.requirements, ", "),
                    currentItem.hybrid.secDescrText,
                    currentItem.hybrid.implicitMods,
                    currentItem.hybrid.explicitMods
                ).forEach {
                    populateItemText(it, hybridItemText, currentItem.frameType)
                }
            }

            FetchedItem(
                item.item.name,
                item.item.typeLine,
                item.item.icon,
                item.item.sockets,
                currentItem.frameType,
                colorId,
                getInfluenceIcons(item.item),
                itemText,
                currentItem.hybrid?.baseTypeName,
                hybridItemText
            )
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun populateItemText(
        properties: Any,
        itemText: SpannableStringBuilder,
        frameType: Int?
    ) {
        if (itemText.isNotEmpty())
            itemText.insertSeparator(getSeparator(frameType))
        when (properties) {
            is List<*> -> {
                itemText.append(properties.joinToString("\n"))
            }
            is String -> {
                itemText.append(properties)
            }
            is SpannableStringBuilder -> {
                itemText.append(properties)
            }
        }
    }

    private fun SpannableStringBuilder.insertSeparator(image: Int) {
        this.appendLine()
        this.append(' ')
        this.setSpan(
            CenteredImageSpan(context, image),
            this.length - 1,
            this.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        this.appendLine()
    }
}