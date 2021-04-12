package com.poe.tradeapp.currency.presentation

import androidx.lifecycle.ViewModel
import com.poe.tradeapp.core.domain.models.NotificationItemData
import com.poe.tradeapp.core.domain.models.NotificationRequest
import com.poe.tradeapp.core.domain.usecases.GetCurrencyItemsUseCase
import com.poe.tradeapp.core.domain.usecases.GetNotificationRequestsUseCase
import com.poe.tradeapp.core.domain.usecases.SetNotificationRequestUseCase
import com.poe.tradeapp.core.presentation.FirebaseUtils
import com.poe.tradeapp.core.presentation.models.NotificationRequestViewData
import com.poe.tradeapp.currency.data.models.CurrencyRequest
import com.poe.tradeapp.currency.data.models.Exchange
import com.poe.tradeapp.currency.data.models.Status
import com.poe.tradeapp.currency.domain.usecases.GetCurrencyExchangeResultUseCase
import com.poe.tradeapp.currency.domain.usecases.GetHavingCurrencyUseCase
import com.poe.tradeapp.currency.domain.usecases.GetRequestingCurrenciesUseCase
import com.poe.tradeapp.currency.domain.usecases.GetTotalResultCountUseCase
import com.poe.tradeapp.currency.presentation.fragments.CurrencyExchangeMainFragment
import com.poe.tradeapp.currency.presentation.models.CurrencyGroupViewData
import com.poe.tradeapp.currency.presentation.models.CurrencyResultViewItem
import com.poe.tradeapp.currency.presentation.models.CurrencyViewData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

internal class CurrencyExchangeViewModel(
    getCurrencyItemsUseCase: GetCurrencyItemsUseCase,
    getRequestingCurrenciesUseCase: GetRequestingCurrenciesUseCase,
    getHavingCurrencyUseCase: GetHavingCurrencyUseCase,
    getTotalResultCountUseCase: GetTotalResultCountUseCase,
    private val getCurrencyResult: GetCurrencyExchangeResultUseCase,
    private val setNotificationRequestUseCase: SetNotificationRequestUseCase,
    private val getNotificationRequestsUseCase: GetNotificationRequestsUseCase
) : ViewModel() {

    val allCurrencies =
        getCurrencyItemsUseCase.execute().filterNot { it.label == null }.map { group ->
            val entries =
                group.items.map { CurrencyViewData(it.id, it.label, it.imageUrl) }
            val isText = group.id.startsWith("Maps") || group.id.startsWith("Cards")
            CurrencyGroupViewData(group.id, group.label, isText, entries)
        }

    val currencyItems = allCurrencies.filterNot {
        it.id.startsWith("Maps")
    } + CurrencyGroupViewData("Maps", "Maps", true, listOf())

    val maps = allCurrencies.filter { it.id.startsWith("Maps") }

    val wantCurrencies = getRequestingCurrenciesUseCase.execute()
    val haveCurrencies = getHavingCurrencyUseCase.execute()

    val viewLoadingState = MutableStateFlow(false)

    val getTotalResultCount = getTotalResultCountUseCase.execute()

    suspend fun requestResult(
        league: String,
        isFulfillable: Boolean,
        minimum: String?,
        position: Int
    ) = withContext(Dispatchers.IO) {
        viewLoadingState.emit(true)
        return@withContext try {
            getCurrencyResult.execute(league, isFulfillable, minimum, position).map { item ->
                CurrencyResultViewItem(
                    item.stock,
                    item.pay,
                    item.get,
                    item.payImageUrl,
                    item.getImageUrl,
                    item.payLabel,
                    item.getLabel,
                    item.accountName,
                    item.lastCharacterName,
                    item.status
                )
            }
        } finally {
            viewLoadingState.emit(false)
        }
    }

    suspend fun sendNotificationRequest(
        buyingItem: CurrencyViewData,
        payingItem: CurrencyViewData,
        payingAmount: Int
    ) = withContext(Dispatchers.IO) {
        viewLoadingState.emit(true)
        val request = NotificationRequest(
            NotificationItemData(buyingItem.label, buyingItem.imageUrl ?: ""),
            NotificationItemData(payingItem.label, payingItem.imageUrl ?: ""),
            payingAmount
        )
        val payload = Json.encodeToString(
            CurrencyRequest.serializer(),
            CurrencyRequest(
                Exchange(
                    status = Status("online"),
                    want = listOf(buyingItem.id),
                    have = listOf(payingItem.id),
                    minimum = null,
                    fulfillable = null,
                    account = null
                )
            )
        )
        val result = setNotificationRequestUseCase.execute(
            request,
            payload,
            0,
            FirebaseUtils.getMessagingToken(),
            FirebaseUtils.getAuthToken()
        )
        viewLoadingState.emit(false)
        return@withContext result
    }

    suspend fun getNotificationRequests() = withContext(Dispatchers.IO) {
        viewLoadingState.emit(true)
        val result = getNotificationRequestsUseCase.execute(
            FirebaseUtils.getMessagingToken(),
            FirebaseUtils.getAuthToken(),
            CurrencyExchangeMainFragment.NOTIFICATION_REQUESTS_TYPE
        ).map {
            NotificationRequestViewData(
                it.buyingItem.itemName,
                it.buyingItem.itemIcon,
                it.payingItem.itemName,
                it.payingItem.itemIcon,
                it.payingAmount
            )
        }
        viewLoadingState.emit(false)
        return@withContext result
    }
}