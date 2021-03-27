package com.poe.tradeapp.exchange.domain.usecases

import com.poe.tradeapp.exchange.domain.IFeatureRepository
import com.poe.tradeapp.exchange.domain.models.*
import kotlinx.serialization.json.*

internal class GetItemsDataListUseCase(private val repository: IFeatureRepository) {

    suspend fun execute(data: String, query: String): List<ItemResultData> {
        val response = repository.getItemsExchangeData(data, query)
        return response["result"]?.jsonArray?.map {
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
                ItemData(properties, requirements, secDescrText, implicitMods, explicitMods, note)
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