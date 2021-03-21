package com.poe.tradeapp.charts_feature.domain.usecases

import com.poe.tradeapp.charts_feature.domain.IFeatureRepository
import com.poe.tradeapp.charts_feature.domain.models.ItemOverviewData
import com.squareup.picasso.Picasso
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

internal class GetItemsOverviewUseCase(private val repository: IFeatureRepository) {

    suspend fun execute(league: String, type: String): List<ItemOverviewData> {
        val result = repository.getItemsOverview(league, type).getValue("lines").jsonArray
        return result.mapNotNull { itemOverview ->
            try {
                val id = itemOverview.jsonObject.getValue("id").jsonPrimitive.content
                val name = itemOverview.jsonObject.getValue("name").jsonPrimitive.content
                val icon = itemOverview.jsonObject.getValue("icon").jsonPrimitive.content
                val tradeId = itemOverview.jsonObject.getValue("detailsId").jsonPrimitive.content
                val chaosValue =
                    itemOverview.jsonObject.getValue("chaosValue").jsonPrimitive.content.toFloat()
                val sparkLine = itemOverview.jsonObject.getValue("sparkline").jsonObject
                val totalChange = sparkLine.getValue("totalChange").jsonPrimitive.content.toFloat()
                val sparkLineData = sparkLine.getValue("data").jsonArray.map {
                    it.jsonPrimitive.content.toFloatOrNull()
                }
                val itemIconForText = Picasso.get().load(icon).get()
                ItemOverviewData(
                    id,
                    name,
                    icon,
                    tradeId,
                    itemIconForText,
                    chaosValue,
                    if (sparkLineData.any { it == null }) {
                        null
                    } else {
                        sparkLineData.filterNotNull()
                    },
                    totalChange
                )
            } catch (e: Exception) {
                null
            }
        }
    }
}