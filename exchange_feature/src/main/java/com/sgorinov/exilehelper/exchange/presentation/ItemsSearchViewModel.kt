package com.sgorinov.exilehelper.exchange.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import com.sgorinov.exilehelper.core.domain.models.NotificationItemData
import com.sgorinov.exilehelper.core.domain.models.NotificationRequest
import com.sgorinov.exilehelper.core.domain.usecases.*
import com.sgorinov.exilehelper.core.presentation.FirebaseUtils
import com.sgorinov.exilehelper.core.presentation.models.NotificationRequestViewData
import com.sgorinov.exilehelper.exchange.data.models.ItemsRequestModel
import com.sgorinov.exilehelper.exchange.data.models.ItemsRequestModelFields
import com.sgorinov.exilehelper.exchange.domain.usecases.GetFiltersUseCase
import com.sgorinov.exilehelper.exchange.domain.usecases.GetItemsResultDataUseCase
import com.sgorinov.exilehelper.exchange.domain.usecases.GetTotalItemsResultCountUseCase
import com.sgorinov.exilehelper.exchange.domain.usecases.SetItemDataUseCase
import com.sgorinov.exilehelper.exchange.presentation.fragments.ItemsSearchMainFragment
import com.sgorinov.exilehelper.exchange.presentation.models.*
import com.sgorinov.exilehelper.exchange.presentation.models.enums.ViewFilters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

internal class ItemsSearchViewModel(
    getCurrencyItemsUseCase: GetCurrencyItemsUseCase,
    getItemsUseCase: GetItemsUseCase,
    getStatsUseCase: GetStatsUseCase,
    getFiltersUseCase: GetFiltersUseCase,
    getTotalItemsResultCountUseCase: GetTotalItemsResultCountUseCase,
    private val getItemsResultDataUseCase: GetItemsResultDataUseCase,
    private val setItemDataUseCase: SetItemDataUseCase,
    private val getNotificationRequestsUseCase: GetNotificationRequestsUseCase,
    private val setNotificationRequestUseCase: SetNotificationRequestUseCase
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

    fun setItemData(type: String?, name: String?) {
        setItemDataUseCase.execute(type, name)
    }

    suspend fun sendNotificationRequest(
        buyingItem: SuggestionItem,
        payingAmount: Int
    ) = withContext(Dispatchers.IO) {
        val request = NotificationRequest(
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

        val priceFilter = ItemsRequestModelFields.Filter(
            disabled = false,
            filters = ItemsRequestModelFields.FilterFields(
                listOf(
                    ItemsRequestModelFields.FilterField(
                        ViewFilters.TradeFilters.BuyoutPrice.id,
                        ItemsRequestModelFields.Price(max = payingAmount)
                    )
                )
            )
        )

        val payload = Json.encodeToString(
            ItemsRequestModel.serializer(),
            ItemsRequestModel().apply {
                query.name = buyingItem.name
                query.type = buyingItem.type
                query.filters = ItemsRequestModelFields.Filters(
                    mapOf(ViewFilters.AllFilters.TradeFilter.id to priceFilter)
                )
            }
        )
        return@withContext try {
            setNotificationRequestUseCase.execute(
                request,
                payload,
                ItemsSearchMainFragment.NOTIFICATION_REQUESTS_TYPE.toInt(),
                FirebaseUtils.getMessagingToken(),
                FirebaseUtils.getAuthToken()
            )
        } catch (e: Exception) {
            false
        } finally {
            viewLoadingState.emit(false)
        }
    }

    suspend fun getNotificationRequests(): List<NotificationRequestViewData> {
        viewLoadingState.emit(true)
        return try {
            withContext(Dispatchers.IO) {
                getNotificationRequestsUseCase.execute(
                    FirebaseUtils.getMessagingToken(),
                    FirebaseUtils.getAuthToken(),
                    ItemsSearchMainFragment.NOTIFICATION_REQUESTS_TYPE
                ).map {
                    NotificationRequestViewData(
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
}