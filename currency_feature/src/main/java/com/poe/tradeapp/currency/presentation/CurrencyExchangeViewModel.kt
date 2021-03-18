package com.poe.tradeapp.currency.presentation

import androidx.lifecycle.ViewModel
import com.poe.tradeapp.core.domain.usecases.GetCurrencyItemsUseCase
import com.poe.tradeapp.core.presentation.dp
import com.poe.tradeapp.currency.domain.usecases.GetCurrencyExchangeResultUseCase
import com.poe.tradeapp.currency.presentation.models.CurrencyResultViewItem
import com.poe.tradeapp.currency.presentation.models.StaticGroupViewData
import com.poe.tradeapp.currency.presentation.models.StaticItemViewData
import com.squareup.picasso.Picasso
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

    suspend fun requestItems() {
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

    suspend fun requestResult() {
        withContext(Dispatchers.IO) {
            val result = getCurrencyResult.execute(wantCurrencies, haveCurrencies).map { result ->
                val payItem = allCurrencies.flatMap {
                    it.staticItems
                }.firstOrNull {
                    it.id == result.payCurrencyId
                }
                val getItem = allCurrencies.flatMap {
                    it.staticItems
                }.firstOrNull {
                    it.id == result.getCurrencyId
                }
                val payIcon = payItem?.imageUrl?.let {
                    Picasso.get().load(it).resize(24.dp, 24.dp).get()
                }
                val getIcon = getItem?.imageUrl?.let {
                    Picasso.get().load(it).resize(24.dp, 24.dp).get()
                }
                CurrencyResultViewItem(
                    result.stock,
                    result.pay,
                    result.get,
                    payIcon,
                    getIcon,
                    payItem?.label,
                    getItem?.label,
                    result.accountName,
                    result.lastCharacterName,
                    result.status
                )
            }
            currencyResultData = result
        }
    }
}