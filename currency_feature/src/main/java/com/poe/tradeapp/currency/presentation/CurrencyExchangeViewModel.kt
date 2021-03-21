package com.poe.tradeapp.currency.presentation

import androidx.lifecycle.ViewModel
import com.poe.tradeapp.core.domain.usecases.GetCurrencyItemsUseCase
import com.poe.tradeapp.currency.domain.usecases.GetCurrencyExchangeResultUseCase
import com.poe.tradeapp.currency.presentation.models.CurrencyResultViewItem
import com.poe.tradeapp.currency.presentation.models.StaticGroupViewData
import com.poe.tradeapp.currency.presentation.models.StaticItemViewData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

internal class CurrencyExchangeViewModel(
    private val getCurrencyItems: GetCurrencyItemsUseCase,
    private val getCurrencyResult: GetCurrencyExchangeResultUseCase
) : ViewModel() {

    val viewLoadingState = MutableStateFlow(true)

    val wantCurrencies = mutableListOf<String>()
    val haveCurrencies = mutableListOf<String>()

    var allCurrencies: List<StaticGroupViewData> = listOf()
        private set

    val currencyItems: List<StaticGroupViewData>
        get() = allCurrencies.filterNot { it.id.startsWith("Maps") } + StaticGroupViewData(
            "Maps",
            "Maps",
            true,
            listOf()
        )

    val maps: List<StaticGroupViewData>
        get() {
            return allCurrencies.filter { it.id.startsWith("Maps") }
        }

    var currencyResultData: List<CurrencyResultViewItem> = listOf()
        private set

    suspend fun requestItems() = withContext(Dispatchers.IO) {
        if (allCurrencies.isEmpty()) {
            viewLoadingState.emit(true)
            allCurrencies =
                getCurrencyItems.execute().filterNot { it.label == null }.map { group ->
                    val entries =
                        group.items.map { StaticItemViewData(it.id, it.label, it.imageUrl) }
                    val isText = group.id.startsWith("Maps") || group.id.startsWith("Cards")
                    StaticGroupViewData(group.id, group.label, isText, entries)
                }
            viewLoadingState.emit(false)
        }
    }

    suspend fun requestResult(league: String) = withContext(Dispatchers.IO) {
        val result = getCurrencyResult.execute(wantCurrencies, haveCurrencies, league)
        currencyResultData = result.map { item ->
            val payItem = allCurrencies.flatMap {
                it.staticItems
            }.firstOrNull {
                it.id == item.payCurrencyId
            }
            val getItem = allCurrencies.flatMap {
                it.staticItems
            }.firstOrNull {
                it.id == item.getCurrencyId
            }
            CurrencyResultViewItem(
                item.stock,
                item.pay,
                item.get,
                payItem?.imageUrl,
                getItem?.imageUrl,
                payItem?.label,
                getItem?.label,
                item.accountName,
                item.lastCharacterName,
                item.status
            )
        }
        return@withContext currencyResultData.isNotEmpty()
    }
}