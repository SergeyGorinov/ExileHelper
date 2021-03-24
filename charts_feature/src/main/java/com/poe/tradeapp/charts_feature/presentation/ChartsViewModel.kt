package com.poe.tradeapp.charts_feature.presentation

import androidx.lifecycle.ViewModel
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.poe.tradeapp.charts_feature.domain.usecases.*
import com.poe.tradeapp.charts_feature.presentation.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import kotlin.math.max

internal class ChartsViewModel(
    private val getCurrencyHistoryUseCase: GetCurrencyHistoryUseCase,
    private val getItemsGroupsUseCase: GetItemsGroupsUseCase,
    private val getCurrenciesOverviewUseCase: GetCurrenciesOverviewUseCase,
    private val getItemsOverviewUseCase: GetItemsOverviewUseCase,
    private val getItemHistoryUseCase: GetItemHistoryUseCase
) : ViewModel() {

    val viewLoadingState = MutableStateFlow(true)

    fun getItemsGroups(): List<ItemGroup> {
        return getItemsGroupsUseCase.execute().map {
            ItemGroup(it.label, it.iconUrl, it.isCurrency, it.type)
        }
    }

    suspend fun getCurrenciesOverview(league: String, type: String) = withContext(Dispatchers.IO) {
        viewLoadingState.emit(true)
        val result = getCurrenciesOverviewUseCase.execute(league, type).map {
            val sellingGraphData = mutableListOf<Entry>().apply {
                for (i in it.sellingSparkLine.indices) {
                    add(Entry(i.toFloat(), it.sellingSparkLine[i]))
                }
            }
            val buyingGraphData = if (it.buyingSparkLine != null) {
                mutableListOf<Entry>().apply {
                    for (i in it.buyingSparkLine.indices) {
                        add(Entry(i.toFloat(), it.buyingSparkLine[i]))
                    }
                }
            } else {
                null
            }
            val sellingGraphDataSet = LineDataSet(sellingGraphData, "Pay graph").apply {
                mode = LineDataSet.Mode.CUBIC_BEZIER
                setDrawValues(false)
                setDrawFilled(true)
                setDrawCircles(false)
                setDrawCircleHole(false)
                setDrawValues(false)
                fillFormatter = IFillFormatter { dataSet, _ -> dataSet.yMin }
            }
            val buyingGraphDataSet = LineDataSet(buyingGraphData, "Get graph").apply {
                mode = LineDataSet.Mode.CUBIC_BEZIER
                setDrawValues(false)
                setDrawFilled(true)
                setDrawCircles(false)
                setDrawCircleHole(false)
                setDrawValues(false)
                fillFormatter = IFillFormatter { dataSet, _ -> dataSet.yMin }
            }
            val sellingData =
                ListingData(it.sellingListingData.listingCount, it.sellingListingData.value)
            val buyingData = if (it.buyingListingData != null) {
                ListingData(it.buyingListingData.listingCount, it.buyingListingData.value)
            } else {
                null
            }
            OverviewViewData(
                it.id,
                it.name,
                it.tradeId,
                it.icon,
                sellingData,
                buyingData,
                sellingGraphDataSet,
                buyingGraphDataSet,
                it.sellingTotalChange,
                it.buyingTotalChange
            )
        }
        viewLoadingState.emit(false)
        return@withContext result
    }

    suspend fun getItemsOverview(league: String, type: String) = withContext(Dispatchers.IO) {
        viewLoadingState.emit(true)
        val result = getItemsOverviewUseCase.execute(league, type).map {
            val graphData = mutableListOf<Entry>().apply {
                for (i in it.sellingSparkLine.indices) {
                    add(Entry(i.toFloat(), it.sellingSparkLine[i]))
                }
            }
            val graphDataSet = LineDataSet(graphData, "Item graph").apply {
                mode = LineDataSet.Mode.CUBIC_BEZIER
                setDrawValues(false)
                setDrawFilled(true)
                setDrawCircles(false)
                setDrawCircleHole(false)
                setDrawValues(false)
                fillFormatter = IFillFormatter { dataSet, _ -> dataSet.yMin }
            }
            OverviewViewData(
                it.id,
                it.name,
                it.tradeId,
                it.icon,
                ListingData(it.sellingListingData.listingCount, it.sellingListingData.value),
                null,
                graphDataSet,
                null,
                it.sellingTotalChange,
                it.buyingTotalChange
            )
        }
        viewLoadingState.emit(false)
        return@withContext result
    }

    suspend fun getCurrencyHistory(
        league: String,
        type: String,
        id: String
    ) = withContext(Dispatchers.IO) {
        viewLoadingState.emit(true)
        val overViewData =
            getCurrenciesOverviewUseCase.execute(league, type).firstOrNull { it.id == id }
        val data = getCurrencyHistoryUseCase.execute(league, type, id)
        val maxDay = max(
            data.payCurrencyGraphData.firstOrNull()?.daysAgo ?: 0,
            data.receiveCurrencyGraphData.first().daysAgo
        )
        val receiveGraphDataSet = getFilteredGraphData(maxDay, data.receiveCurrencyGraphData, true)
        val payGraphDataSet = getFilteredGraphData(maxDay, data.payCurrencyGraphData, false)
        val payValue = if (overViewData?.sellingListingData?.value != null) {
            1 / overViewData.sellingListingData.value.toFloat()
        } else {
            null
        }
        val receiveValue = overViewData?.buyingListingData?.value?.toFloat() ?: 0f
        viewLoadingState.emit(false)
        return@withContext HistoryModel(
            overViewData?.name ?: "",
            overViewData?.icon,
            overViewData?.tradeId,
            payGraphDataSet,
            receiveGraphDataSet,
            payValue,
            receiveValue
        )
    }

    suspend fun getItemHistory(
        league: String,
        type: String,
        id: String
    ) = withContext(Dispatchers.IO) {
        viewLoadingState.emit(true)
        val overViewData =
            getItemsOverviewUseCase.execute(league, type).firstOrNull { it.id == id }
        val data = getItemHistoryUseCase.execute(league, type, id)
        val graphData = getFilteredGraphData(data.maxOf { it.daysAgo }, data, true)
        viewLoadingState.emit(false)
        return@withContext HistoryModel(
            overViewData?.name ?: "",
            overViewData?.icon,
            overViewData?.tradeId,
            null,
            graphData,
            null,
            overViewData?.sellingListingData?.value?.toFloat() ?: 0f
        )
    }

    private fun getFilteredGraphData(
        maxDay: Int,
        unfilteredData: List<com.poe.tradeapp.charts_feature.domain.models.GraphData>,
        isReceiveData: Boolean
    ): LineDataSet {
        val filteredGraphData = unfilteredData.filter { it.daysAgo <= 50 }
        val payDataTakeAmount = if (filteredGraphData.size / 10 > 0) {
            filteredGraphData.size / 10
        } else {
            1
        }
        val data = mutableListOf<GraphData>()
        var i = 0
        do {
            val entry = filteredGraphData[i]
            data.add(
                GraphData(
                    entry.count,
                    if (isReceiveData) entry.value.toFloat() else 1 / entry.value.toFloat(),
                    entry.daysAgo
                )
            )
            i += payDataTakeAmount
        } while (i < filteredGraphData.size)
        if (data.last().daysAgo != 0) {
            data.add(
                GraphData(
                    data.last().count,
                    if (isReceiveData) data.last().value else 1 / data.last().value,
                    data.last().daysAgo
                )
            )
        }
        return data.map { Entry((maxDay - it.daysAgo).toFloat(), it.value) }.run {
            LineDataSet(this, "dataSet").apply {
                mode = LineDataSet.Mode.HORIZONTAL_BEZIER
                circleRadius = 4f
                lineWidth = 3f
                isHighlightEnabled = true
            }
        }
    }
}