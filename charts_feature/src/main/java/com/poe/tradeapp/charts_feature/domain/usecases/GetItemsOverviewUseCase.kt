package com.poe.tradeapp.charts_feature.domain.usecases

import com.poe.tradeapp.charts_feature.domain.IFeatureRepository
import com.poe.tradeapp.charts_feature.domain.models.CurrencyData
import com.poe.tradeapp.charts_feature.domain.models.OverviewData
import kotlinx.serialization.json.*

internal class GetItemsOverviewUseCase(private val repository: IFeatureRepository) {

    suspend fun execute(league: String, type: String): List<OverviewData> {
        val result = try {
            repository.getItemsOverview(league, type).getValue("lines").jsonArray
        } catch (e: Exception) {
            return emptyList()
        }
        return result.mapNotNull { itemOverview ->
            try {
                val id = itemOverview.jsonObject.getValue("id").jsonPrimitive.content
                val name = itemOverview.jsonObject.getValue("name").jsonPrimitive.content
                val icon = itemOverview.jsonObject.getValue("icon").jsonPrimitive.content
                val tradeId = itemOverview.jsonObject.getValue("detailsId").jsonPrimitive.content
                val listingCount =
                    itemOverview.jsonObject.getValue("listingCount").jsonPrimitive.intOrNull ?: 0
                val chaosValue =
                    itemOverview.jsonObject.getValue("chaosValue").jsonPrimitive.doubleOrNull ?: 0.0
                val sparkLine = itemOverview.jsonObject.getValue("sparkline").jsonObject
                val totalChange = sparkLine.getValue("totalChange").jsonPrimitive.floatOrNull ?: 0f
                val sparkLineData = sparkLine.getValue("data").jsonArray.map {
                    it.jsonPrimitive.content.toFloatOrNull()
                }
                OverviewData(
                    id,
                    name,
                    icon,
                    tradeId,
                    null,
                    CurrencyData(listingCount, chaosValue),
                    null,
                    if (sparkLineData.any { it == null }) {
                        listOf()
                    } else {
                        sparkLineData.filterNotNull()
                    },
                    null,
                    totalChange
                )
            } catch (e: Exception) {
                null
            }
        }
    }
}