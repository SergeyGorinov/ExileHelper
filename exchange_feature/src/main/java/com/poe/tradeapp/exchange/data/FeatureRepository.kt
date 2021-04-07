package com.poe.tradeapp.exchange.data

import android.util.Log
import com.poe.tradeapp.exchange.data.models.Filter
import com.poe.tradeapp.exchange.data.models.ItemsRequestModel
import com.poe.tradeapp.exchange.data.models.ItemsRequestModelFields
import com.poe.tradeapp.exchange.domain.models.*
import kotlinx.serialization.json.*
import retrofit2.await

internal class FeatureRepository(private val apiService: ApiService) : BaseFeatureRepository() {

    override val filters = mutableListOf<Filter>()
    override var totalItemsResultCount: Int = 0

    private var type: String? = null
    private var name: String? = null
    private var id: String = ""
    private var itemResultListData: List<String> = listOf()

    override fun setItemData(type: String, name: String?) {
        this.type = type
        this.name = name
    }

    override suspend fun getItemsExchangeData(
        league: String,
        position: Int
    ): List<ItemResultData> {
        if (position == 0) {
            fetchAllEntries(league)
        }

        val data = getRequestData(position)
        val result = apiService.getItemExchangeResponse(data, id).await()

        return processResult(result)
    }

    private suspend fun fetchAllEntries(league: String) {
        val response = apiService.getItemsExchangeList(league, populateRequest()).await()
        id = response["id"]?.jsonPrimitive?.content ?: ""
        itemResultListData = response["result"]?.jsonArray?.map {
            it.jsonPrimitive.content
        } ?: listOf()
        totalItemsResultCount = itemResultListData.size
    }

    private fun getRequestData(position: Int): String {
        val subList = if (itemResultListData.size - position > 10) {
            itemResultListData.subList(position, position + 10)
        } else
            itemResultListData.subList(position, position + (itemResultListData.size - position))
        return subList.joinToString(",")
    }

    private fun populateRequest(): ItemsRequestModel {
        val requestModel = ItemsRequestModel()

        requestModel.query.name = name
        requestModel.query.type = type

        val reqFilters = mutableMapOf<String, ItemsRequestModelFields.Filter?>()

        filters.forEach { filter ->
            var reqFilter: ItemsRequestModelFields.Filter? = null
            if (filter.fields.any { a -> a.value != null }) {
                reqFilter = ItemsRequestModelFields.Filter(!filter.isEnabled)
                reqFilter.filters = ItemsRequestModelFields.FilterFields(filter.fields.map { m ->
                    ItemsRequestModelFields.FilterField(
                        m.name,
                        m.value
                    )
                })
            }
            reqFilters[filter.name] = reqFilter
        }

        requestModel.query.filters = ItemsRequestModelFields.Filters(reqFilters)

        return requestModel
    }

    private fun processResult(response: JsonObject): List<ItemResultData> {
        return response["result"]?.jsonArray?.mapNotNull {
            try {
                val result = it.jsonObject
                val item = result["item"]?.jsonObject

                val sockets = item?.get("sockets")?.jsonArray
                val influences = item?.get("influences")?.jsonObject
                val properties = parsePropertyData(item?.get("properties")?.jsonArray)
                val requirements = parsePropertyData(item?.get("requirements")?.jsonArray)

                val hybrid = item?.get("hybrid")?.jsonObject
                val hybridSecDescrText = hybrid?.get("secDescrText")?.jsonPrimitive?.content
                val hybridIsVaalGem = hybrid?.get("isVaalGem")?.jsonPrimitive?.booleanOrNull
                val hybridBaseTypeName = hybrid?.get("baseTypeName")?.jsonPrimitive?.content
                val hybridProperties = parsePropertyData(hybrid?.get("properties")?.jsonArray)
                val hybridRequirements = parsePropertyData(hybrid?.get("requirements")?.jsonArray)
                val hybridImplicitMods =
                    hybrid?.get("implicitMods")?.jsonArray?.map { mod -> mod.jsonPrimitive.content }
                val hybridExplicitMods =
                    hybrid?.get("explicitMods")?.jsonArray?.map { mod -> mod.jsonPrimitive.content }

                val name = item?.get("name")?.jsonPrimitive?.content ?: ""
                val typeLine = item?.get("typeLine")?.jsonPrimitive?.content ?: ""
                val icon = item?.get("icon")?.jsonPrimitive?.content ?: ""
                val frameType = item?.get("frameType")?.jsonPrimitive?.intOrNull
                val synthesised = item?.get("synthesised")?.jsonPrimitive?.booleanOrNull
                val replica = item?.get("replica")?.jsonPrimitive?.booleanOrNull
                val corrupted = item?.get("corrupted")?.jsonPrimitive?.booleanOrNull
                val secDescrText = item?.get("secDescrText")?.jsonPrimitive?.content
                val note = item?.get("note")?.jsonPrimitive?.content

                val elder = influences?.get("elder")?.jsonPrimitive?.booleanOrNull
                val shaper = influences?.get("shaper")?.jsonPrimitive?.booleanOrNull
                val warlord = influences?.get("warlord")?.jsonPrimitive?.booleanOrNull
                val hunter = influences?.get("hunter")?.jsonPrimitive?.booleanOrNull
                val redeemer = influences?.get("redeemer")?.jsonPrimitive?.booleanOrNull
                val crusader = influences?.get("crusader")?.jsonPrimitive?.booleanOrNull

                val implicitMods =
                    item?.get("implicitMods")?.jsonArray?.map { mod -> mod.jsonPrimitive.content }
                val explicitMods =
                    item?.get("explicitMods")?.jsonArray?.map { mod -> mod.jsonPrimitive.content }

                val socketsData = sockets?.map { socket ->
                    val socketData = socket.jsonObject
                    val group = socketData["group"]?.jsonPrimitive?.intOrNull ?: 0
                    val attribute = socketData["attr"]?.jsonPrimitive?.content ?: ""
                    val color = socketData["sColour"]?.jsonPrimitive?.content ?: ""
                    Socket(group, attribute, color)
                }

                val itemData =
                    ItemData(
                        properties,
                        requirements,
                        secDescrText,
                        implicitMods,
                        explicitMods,
                        note
                    )
                val hybridData = HybridData(
                    hybridIsVaalGem,
                    hybridProperties,
                    hybridRequirements,
                    hybridSecDescrText,
                    hybridImplicitMods,
                    hybridExplicitMods,
                    null
                )
                ItemResultData(
                    name,
                    typeLine,
                    icon,
                    frameType,
                    synthesised,
                    replica,
                    corrupted,
                    socketsData,
                    hybridBaseTypeName,
                    Influences(elder, shaper, warlord, hunter, redeemer, crusader),
                    itemData,
                    hybridData
                )
            } catch (e: Exception) {
                Log.e("Process result failed!", e.stackTraceToString())
                null
            }
        } ?: return emptyList()
    }

    private fun parsePropertyData(data: JsonArray?): List<Property> {
        data ?: return emptyList()

        return data.map {
            val property = it.jsonObject
            val propertyName = property["name"]?.jsonPrimitive?.content ?: ""
            val displayMode = property["displayMode"]?.jsonPrimitive?.intOrNull ?: 0
            val progress = property["progress"]?.jsonPrimitive?.doubleOrNull
            val type = property["type"]?.jsonPrimitive?.intOrNull
            val suffix = property["suffix"]?.jsonPrimitive?.content
            val values = property["values"]?.jsonArray?.map { value ->
                val valueData = value.jsonArray
                val propertyValue = valueData[0].jsonPrimitive.content
                val propertyColor = valueData[1].jsonPrimitive.int
                PropertyData(propertyValue, propertyColor)
            } ?: listOf()
            Property(propertyName, values, displayMode, progress, type, suffix)
        }
    }
}