package com.sgorinov.exilehelper.currency.presentation

import androidx.lifecycle.ViewModel
import com.sgorinov.exilehelper.core.domain.models.NotificationItemData
import com.sgorinov.exilehelper.core.domain.models.NotificationRequest
import com.sgorinov.exilehelper.core.domain.usecases.GetCurrencyItemsUseCase
import com.sgorinov.exilehelper.core.domain.usecases.GetNotificationRequestsUseCase
import com.sgorinov.exilehelper.core.domain.usecases.SetNotificationRequestUseCase
import com.sgorinov.exilehelper.core.presentation.FirebaseUtils
import com.sgorinov.exilehelper.core.presentation.models.NotificationRequestViewData
import com.sgorinov.exilehelper.currency.data.models.CurrencyRequest
import com.sgorinov.exilehelper.currency.data.models.Exchange
import com.sgorinov.exilehelper.currency.data.models.Status
import com.sgorinov.exilehelper.currency.domain.usecases.GetCurrencyExchangeResultUseCase
import com.sgorinov.exilehelper.currency.domain.usecases.GetHavingCurrencyUseCase
import com.sgorinov.exilehelper.currency.domain.usecases.GetRequestingCurrenciesUseCase
import com.sgorinov.exilehelper.currency.domain.usecases.GetTotalResultCountUseCase
import com.sgorinov.exilehelper.currency.presentation.fragments.CurrencyExchangeMainFragment
import com.sgorinov.exilehelper.currency.presentation.models.CurrencyGroupViewData
import com.sgorinov.exilehelper.currency.presentation.models.CurrencyResultViewItem
import com.sgorinov.exilehelper.currency.presentation.models.CurrencyViewData
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
        payingAmount: Int,
        league: String
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
            league,
            FirebaseUtils.getAuthToken()
        )
        viewLoadingState.emit(false)
        return@withContext result
    }

    suspend fun getNotificationRequests(league: String) = withContext(Dispatchers.IO) {
        viewLoadingState.emit(true)
        val result = getNotificationRequestsUseCase.execute(
            FirebaseUtils.getMessagingToken(),
            FirebaseUtils.getAuthToken(),
            CurrencyExchangeMainFragment.NOTIFICATION_REQUESTS_TYPE,
            league
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