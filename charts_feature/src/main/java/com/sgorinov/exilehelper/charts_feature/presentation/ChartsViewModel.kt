package com.sgorinov.exilehelper.charts_feature.presentation

import androidx.lifecycle.ViewModel
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.sgorinov.exilehelper.charts_feature.domain.usecases.*
import com.sgorinov.exilehelper.charts_feature.presentation.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import kotlin.math.max

internal class ChartsViewModel(
    private val getCurrencyHistoryUseCase: GetCurrencyHistoryUseCase,
    private val getItemsGroupsUseCase: GetItemsGroupsUseCase,
    private val getCurrenciesOverviewUseCase: GetCurrenciesOverviewUseCase,
    private val getItemsOverviewUseCase: GetItemsOverviewUseCase,
    private val getItemHistoryUseCase: GetItemHistoryUseCase,
    getOverviewUseCase: GetOverviewUseCase
) : ViewModel() {

    val viewLoadingState = MutableStateFlow(false)

    val overviewData = getOverviewUseCase.execute().map {
        val sellingGraphData = if (it.sellingSparkLine != null) {
            mutableListOf<Entry>().apply {
                for (i in it.sellingSparkLine.indices) {
                    add(Entry(i.toFloat(), it.sellingSparkLine[i]))
                }
            }
        } else {
            null
        }
        val buyingGraphData = mutableListOf<Entry>().apply {
            for (i in it.buyingSparkLine.indices) {
                add(Entry(i.toFloat(), it.buyingSparkLine[i]))
            }
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
        val sellingData = if (it.sellingListingData != null) {
            ListingData(it.sellingListingData.listingCount, it.sellingListingData.value)
        } else {
            null
        }
        val buyingData =
            ListingData(it.buyingListingData.listingCount, it.buyingListingData.value)
        OverviewViewData(
            it.id,
            it.name,
            it.type,
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

    fun getItemsGroups(): List<ItemGroup> {
        return getItemsGroupsUseCase.execute().map {
            ItemGroup(it.label, it.iconUrl, it.isCurrency, it.type)
        }
    }

    suspend fun getCurrenciesOverview(league: String, type: String) {
        withContext(Dispatchers.IO) {
            viewLoadingState.emit(true)
            getCurrenciesOverviewUseCase.execute(league, type)
            viewLoadingState.emit(false)
        }
    }

    suspend fun getItemsOverview(league: String, type: String) {
        withContext(Dispatchers.IO) {
            viewLoadingState.emit(true)
            getItemsOverviewUseCase.execute(league, type)
            viewLoadingState.emit(false)
        }
    }

    suspend fun getCurrencyHistory(
        league: String,
        type: String,
        id: String
    ) = withContext(Dispatchers.IO) {
        viewLoadingState.emit(true)
        val overViewData = overviewData.firstOrNull { it.id == id }
        val data = getCurrencyHistoryUseCase.execute(league, type, id)
        val maxDay = max(
            data.buyingGraphData?.filter { it.daysAgo <= 50 }?.maxOfOrNull { it.daysAgo } ?: 0,
            data.sellingGraphData.filter { it.daysAgo <= 50 }.maxOfOrNull { it.daysAgo } ?: 0
        )
        val sellingGraphDataSet = getFilteredGraphData(maxDay, data.sellingGraphData, true)
        val buyingGraphDataSet = if (data.buyingGraphData != null) {
            getFilteredGraphData(maxDay, data.buyingGraphData, false)
        } else {
            null
        }
        val sellingValue = overViewData?.sellingListingData?.value?.toFloat() ?: 0f
        val buyingValue = overViewData?.buyingListingData?.value?.toFloat() ?: 0f
        viewLoadingState.emit(false)
        HistoryModel(
            overViewData?.name ?: "",
            null,
            overViewData?.icon,
            overViewData?.tradeId,
            sellingGraphDataSet,
            buyingGraphDataSet,
            sellingValue,
            buyingValue,
            overViewData?.sellingSparkLine,
            overViewData?.buyingSparkLine ?: LineDataSet(emptyList(), ""),
            overViewData?.sellingTotalChange,
            overViewData?.buyingTotalChange ?: 0f
        )
    }

    suspend fun getItemHistory(
        league: String,
        type: String,
        id: String
    ) = withContext(Dispatchers.IO) {
        viewLoadingState.emit(true)
        val overViewData = overviewData.firstOrNull { it.id == id }
        val data = getItemHistoryUseCase.execute(league, type, id)
        val graphData = getFilteredGraphData(
            data.filter {
                it.daysAgo <= 50
            }.maxOfOrNull {
                it.daysAgo
            } ?: 0,
            data,
            false
        )
        viewLoadingState.emit(false)
        HistoryModel(
            overViewData?.name ?: "",
            overViewData?.type,
            overViewData?.icon,
            overViewData?.tradeId,
            null,
            graphData,
            null,
            overViewData?.buyingListingData?.value?.toFloat() ?: 0f,
            overViewData?.sellingSparkLine,
            overViewData?.buyingSparkLine ?: LineDataSet(emptyList(), ""),
            overViewData?.sellingTotalChange,
            overViewData?.buyingTotalChange ?: 0f
        )
    }

    private fun getFilteredGraphData(
        maxDay: Int,
        unfilteredData: List<com.sgorinov.exilehelper.charts_feature.domain.models.GraphData>,
        selling: Boolean
    ): LineDataSet {
        val filteredGraphData =
            unfilteredData.filter { it.daysAgo <= 50 && it.count >= 10 }
        val takeAmount = if (filteredGraphData.size / 10 > 0) {
            filteredGraphData.size / 10
        } else {
            1
        }
        val data = filteredGraphData.chunked(takeAmount).map { chunked ->
            val value = chunked.sumByDouble { it.value }.toFloat() / chunked.size
            GraphData(
                chunked.sumBy { it.count } / chunked.size,
                if (selling) 1 / value else value,
                chunked.first().daysAgo
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