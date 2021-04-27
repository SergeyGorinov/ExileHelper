package com.sgorinov.exilehelper.exchange_feature.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import com.sgorinov.exilehelper.core.domain.models.NotificationItemData
import com.sgorinov.exilehelper.core.domain.models.NotificationRequest
import com.sgorinov.exilehelper.core.domain.usecases.*
import com.sgorinov.exilehelper.core.presentation.FirebaseUtils
import com.sgorinov.exilehelper.core.presentation.models.NotificationRequestViewData
import com.sgorinov.exilehelper.exchange_feature.data.models.*
import com.sgorinov.exilehelper.exchange_feature.domain.usecases.GetFiltersUseCase
import com.sgorinov.exilehelper.exchange_feature.domain.usecases.GetItemsResultDataUseCase
import com.sgorinov.exilehelper.exchange_feature.domain.usecases.GetTotalItemsResultCountUseCase
import com.sgorinov.exilehelper.exchange_feature.domain.usecases.SetItemDataUseCase
import com.sgorinov.exilehelper.exchange_feature.presentation.fragments.ItemsSearchMainFragment
import com.sgorinov.exilehelper.exchange_feature.presentation.models.*
import com.sgorinov.exilehelper.exchange_feature.presentation.models.enums.ViewFilters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

internal class ItemsSearchViewModel(
    getItemsUseCase: GetItemsUseCase,
    getStatsUseCase: GetStatsUseCase,
    getTotalItemsResultCountUseCase: GetTotalItemsResultCountUseCase,
    private val getFiltersUseCase: GetFiltersUseCase,
    private val getItemsResultDataUseCase: GetItemsResultDataUseCase,
    private val setItemDataUseCase: SetItemDataUseCase,
    private val getNotificationRequestsUseCase: GetNotificationRequestsUseCase,
    private val setNotificationRequestUseCase: SetNotificationRequestUseCase,
    private val removeRequestUseCase: RemoveRequestUseCase
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

    val uniques = listOf(ViewFilters.ItemDropdownFilter(null, "Any")) + itemGroups.flatMap {
        it.entries
    }.filter {
        it.flags?.unique == true
    }.map {
        ViewFilters.ItemDropdownFilter(it.name, it.text)
    }

    private val cards = listOf(ViewFilters.ItemDropdownFilter(null, "Any")) +
            (itemGroups.firstOrNull { it.label == "Cards" }?.entries ?: listOf()).map {
                ViewFilters.ItemDropdownFilter(it.text, it.text)
            }

    private val currencies = listOf(ViewFilters.ItemDropdownFilter(null, "Any")) +
            (itemGroups.firstOrNull { it.label == "Currency" }?.entries ?: listOf()).map {
                ViewFilters.ItemDropdownFilter(it.text, it.text)
            }

    val ultimatumInput = uniques + cards + currencies

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

    fun getFilters() = getFiltersUseCase.execute()

    val totalItemsCount = getTotalItemsResultCountUseCase.execute()

    suspend fun fetchPartialResults(league: String, position: Int): List<ItemResultViewData> {
        return withContext(Dispatchers.IO) {
            try {
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
            } catch (e: Exception) {
                Log.e("ERROR", e.stackTraceToString())
                listOf()
            }
        }
    }

    fun setItemData(type: String?, name: String?) {
        setItemDataUseCase.execute(type, name)
    }

    suspend fun sendNotificationRequest(
        buyingItem: SuggestionItem,
        payingAmount: Int,
        league: String
    ) = withContext(Dispatchers.IO) {
        val request = NotificationRequest(
            null,
            NotificationItemData(
                "${buyingItem.name}${if (buyingItem.name != null) " " else ""}${buyingItem.type}",
                ""
            ),
            NotificationItemData(
                "Chaos Orb",
                "https://www.pathofexile.com/image/Art/2DItems/Currency/CurrencyRerollRare.png?v=c60aa876dd6bab31174df91b1da1b4f9"
            ),
            payingAmount
        )

        val priceFilter = Filter(
            disabled = false,
            filters = FilterFields(
                listOf(
                    FilterField(
                        ViewFilters.TradeFilters.BuyoutPrice.id,
                        Price(max = payingAmount)
                    )
                )
            )
        )

        val payload = Json.encodeToString(
            ItemsRequestModel.serializer(),
            ItemsRequestModel().apply {
                query.name = buyingItem.name
                query.type = buyingItem.type
                query.filters = Filters(
                    mapOf("trade_filters" to priceFilter)
                )
            }
        )
        return@withContext try {
            setNotificationRequestUseCase.execute(
                request,
                payload,
                ItemsSearchMainFragment.NOTIFICATION_REQUESTS_TYPE.toInt(),
                FirebaseUtils.getMessagingToken(),
                league,
                FirebaseUtils.getAuthToken()
            )
        } catch (e: Exception) {
            false
        } finally {
            viewLoadingState.emit(false)
        }
    }

    suspend fun getNotificationRequests(league: String): List<NotificationRequestViewData> {
        viewLoadingState.emit(true)
        return try {
            withContext(Dispatchers.IO) {
                getNotificationRequestsUseCase.execute(
                    FirebaseUtils.getMessagingToken(),
                    FirebaseUtils.getAuthToken(),
                    ItemsSearchMainFragment.NOTIFICATION_REQUESTS_TYPE,
                    league
                ).map {
                    NotificationRequestViewData(
                        it.id,
                        it.buyingItem.itemName,
                        it.buyingItem.itemIcon,
                        it.payingItem.itemName,
                        it.payingItem.itemIcon,
                        it.payingAmount
                    )
                }
            }
        } catch (e: Exception) {
            Log.e("getNotificationRequests", e.stackTraceToString())
            emptyList()
        } finally {
            viewLoadingState.emit(false)
        }
    }

    suspend fun removeRequest(id: Long): Boolean {
        viewLoadingState.emit(true)
        val result = removeRequestUseCase.execute(id)
        viewLoadingState.emit(false)
        return result != null && result.isSuccessful
    }
}