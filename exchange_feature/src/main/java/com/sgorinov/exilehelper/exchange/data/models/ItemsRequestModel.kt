package com.sgorinov.exilehelper.exchange.data.models

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonObject

@Serializable
data class ItemsRequestModel(
    @Required
    val query: ItemsRequestModelFields.Query = ItemsRequestModelFields.Query(),
    @Required
    val sort: ItemsRequestModelFields.Sorting = ItemsRequestModelFields.Sorting()
)

class ItemsRequestModelFields {

    @Serializable
    data class Query(
        @Required
        var status: Status = Status(),
        @Required
        val stats: MutableList<Stat> = mutableListOf(Stat()),
        var name: String? = null,
        var type: String? = null,
        @Required
        var filters: Filters = Filters()
    )

    @Serializable
    data class Sorting(
        @Required
        var price: String? = "asc",
        var quality: String? = null,
        var pdamage: String? = null,
        var crit: String? = null,
        var aps: String? = null,
        var ilvl: String? = null,
        var pdps: String? = null,
        var edamage: String? = null,
        var edps: String? = null,
        var dps: String? = null,
        var ev: String? = null,
        var ar: String? = null,
        var es: String? = null,
        var block: String? = null
    )

    @Serializable
    data class Status(
        @Required
        var option: String = "online"
    )

    @Serializable
    data class Stat(
        @Required
        var type: String = "and",
        @Required
        var filters: MutableList<StatFilter> = mutableListOf()
    )

    @Serializable
    data class StatFilter(
        var id: String,
        var value: MinMax? = null,
        var disabled: Boolean
    )

    @Serializable(with = FiltersSerializer::class)
    data class Filters(
        @Required
        val map: Map<String, Filter?> = mapOf()
    )

    @Serializable
    data class Filter(
        @Required
        var disabled: Boolean = true,
        @Required
        var filters: FilterFields? = null
    )

    @Serializable(with = FilterFieldsSerializer::class)
    data class FilterFields(
        val fields: List<FilterField>
    )

    @Serializable
    data class FilterField(
        val name: String,
        @Contextual
        val value: Any?
    )

    @Serializable
    data class Sockets(
        var r: Int? = null,
        var g: Int? = null,
        var b: Int? = null,
        var w: Int? = null,
        var min: Int? = null,
        var max: Int? = null
    ) {
        fun isEmpty() = listOfNotNull(r, g, b, w, min, max).isEmpty()
    }

    @Serializable
    data class MinMax(
        var min: Int? = null,
        var max: Int? = null
    ) {
        fun isEmpty() = listOfNotNull(min, max).isEmpty()
    }

    @Serializable
    data class DropDown(
        var option: String? = null
    )

    @Serializable
    data class Price(
        var min: Int? = null,
        var max: Int? = null,
        var option: String? = null
    ) {
        fun isEmpty() = listOfNotNull(min, max, option).isEmpty()
    }

    private object AnySerializer : KSerializer<Any> {

        @Suppress("UNCHECKED_CAST")
        private val anyDataTypeSerializers = mapOf(
            "String" to serializer<String>(),
            "Int" to serializer<Int>(),
            "Sockets" to Sockets.serializer(),
            "MinMax" to MinMax.serializer(),
            "DropDown" to DropDown.serializer(),
            "Price" to Price.serializer()
        ).mapValues { (_, v) ->
            v as KSerializer<Any>
        }

        private fun getValueSerializer(dataType: String): KSerializer<Any> {
            return anyDataTypeSerializers[dataType] ?: throw SerializationException(
                "Serializer for class $dataType is not registered in AnySerializer"
            )
        }

        override val descriptor = buildClassSerialDescriptor("Any")

        override fun deserialize(decoder: Decoder): Any {
            return ""
        }

        override fun serialize(encoder: Encoder, value: Any) {
            val dataType = when (value) {
                is String -> "String"
                is Int -> "Int"
                is Sockets -> "Sockets"
                is MinMax -> "MinMax"
                is DropDown -> "DropDown"
                is Price -> "Price"
                else -> "Any"
            }
            encoder.encodeSerializableValue(getValueSerializer(dataType), value)
        }
    }

    private object FilterFieldsSerializer : KSerializer<FilterFields> {

        override val descriptor: SerialDescriptor = buildClassSerialDescriptor("FilterFields")

        override fun deserialize(decoder: Decoder): FilterFields {
            return FilterFields(listOf())
        }

        override fun serialize(encoder: Encoder, value: FilterFields) {
            val jsonEncoder = encoder as JsonEncoder
            val fieldsMap = mutableMapOf<String, JsonElement>()
            value.fields.forEach { field ->
                field.value?.let {
                    val element = jsonEncoder.json.encodeToJsonElement(AnySerializer, it)
                    fieldsMap[field.name] = element
                }
            }
            jsonEncoder.encodeJsonElement(JsonObject(fieldsMap))
        }
    }

    private object FiltersSerializer : KSerializer<Filters> {

        override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Filters")

        override fun deserialize(decoder: Decoder): Filters {
            return Filters()
        }

        override fun serialize(encoder: Encoder, value: Filters) {
            val jsonEncoder = encoder as JsonEncoder
            val filtersMap = mutableMapOf<String, JsonElement>()
            value.map.forEach { filter ->
                filter.value?.let {
                    val element = jsonEncoder.json.encodeToJsonElement(Filter.serializer(), it)
                    filtersMap[filter.key] = element
                }
            }
            jsonEncoder.encodeJsonElement(JsonObject(filtersMap))
        }
    }
}