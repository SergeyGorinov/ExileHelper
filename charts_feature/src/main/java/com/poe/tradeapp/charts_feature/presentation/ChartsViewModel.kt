package com.poe.tradeapp.charts_feature.presentation

import androidx.lifecycle.ViewModel
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.poe.tradeapp.charts_feature.domain.usecases.GetCurrencyHistoryUseCase
import com.poe.tradeapp.charts_feature.domain.usecases.GetItemsGroupsUseCase
import com.poe.tradeapp.charts_feature.presentation.models.CurrencyHistoryModel
import com.poe.tradeapp.charts_feature.presentation.models.GraphData
import com.poe.tradeapp.charts_feature.presentation.models.ItemGroup
import kotlin.math.max

internal class ChartsViewModel(
    private val getCurrencyHistoryUseCase: GetCurrencyHistoryUseCase,
    private val getItemsGroupsUseCase: GetItemsGroupsUseCase
) : ViewModel() {

    suspend fun getCurrencyHistory(
        league: String,
        type: String,
        id: String
    ): CurrencyHistoryModel {
        val data = getCurrencyHistoryUseCase.execute(league, type, id)
        val maxDay = max(
            data.payCurrencyGraphData.first().daysAgo,
            data.receiveCurrencyGraphData.first().daysAgo
        )
        val receiveData = data.receiveCurrencyGraphData.map {
            GraphData(it.count, it.value.toFloat(), it.daysAgo)
        }
        val payData = data.payCurrencyGraphData.mapNotNull { payDataEntry ->
            receiveData.firstOrNull {
                payDataEntry.daysAgo == it.daysAgo
            }?.let { receiveDataEntry ->
                val value = payDataEntry.count / receiveDataEntry.count / payDataEntry.value
                GraphData(payDataEntry.count, value.toFloat(), payDataEntry.daysAgo)
            }
        }
        val receiveGraphData = receiveData.map {
            Entry((maxDay - it.daysAgo).toFloat(), it.value)
        }
        val receiveGraphDataSet = LineDataSet(receiveGraphData, "receiveDataSet").apply {
            mode = LineDataSet.Mode.CUBIC_BEZIER
            circleRadius = 4f
            lineWidth = 3f
            isHighlightEnabled = true
            setDrawValues(false)
        }

        val payGraphDataSet = payData.map {
            Entry((maxDay - it.daysAgo).toFloat(), it.value)
        }.run {
            LineDataSet(this, "payDataSet").apply {
                mode = LineDataSet.Mode.CUBIC_BEZIER
                circleRadius = 4f
                lineWidth = 3f
                isHighlightEnabled = true
                setDrawValues(false)
            }
        }

        return CurrencyHistoryModel(payGraphDataSet, receiveGraphDataSet)
    }

    fun getItemsGroups(): List<ItemGroup> {
        return getItemsGroupsUseCase.execute().map {
            ItemGroup(it.label, it.iconUrl, it.isCurrency, it.type)
        }
    }
}