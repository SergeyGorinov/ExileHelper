package com.poetradeapp.models.request

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class CustomSerializer :
    StdSerializer<ItemsRequestModelFields.FilterFields>(ItemsRequestModelFields.FilterFields::class.java) {
    override fun serialize(
        value: ItemsRequestModelFields.FilterFields?,
        gen: JsonGenerator?,
        provider: SerializerProvider?
    ) {
        if (value != null && gen != null) {
            gen.writeStartObject()
            value.fields.forEach { field ->
                if (field.value != null) {
                    gen.writeObjectField(field.name, field.value)
                }
            }
            gen.writeEndObject()
        }
    }
}

@ExperimentalCoroutinesApi
@JsonInclude
data class ItemsRequestModel(
    val query: ItemsRequestModelFields.Query = ItemsRequestModelFields.Query(),
    val sort: ItemsRequestModelFields.Sorting = ItemsRequestModelFields.Sorting()
)

@ExperimentalCoroutinesApi
class ItemsRequestModelFields {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class Query(
        var status: Status = Status(),
        val stats: MutableList<Stat> = mutableListOf(Stat()),
        var name: String? = null,
        var type: String? = null,
        var filters: Filters? = null
    )

    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class Sorting(
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

    @JsonInclude
    data class Status(
        var option: String = "online"
    )

    @JsonInclude
    data class Stat(
        var type: String = "and",
        var filters: MutableList<StatFilter> = mutableListOf()
    )

    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class StatFilter(
        var id: String,
        var value: MinMax?,
        var disabled: Boolean
    )

    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class Filters(@JsonIgnore val map: Map<String, Filter?>) {
        val type_filters: Filter? by map
        val weapon_filters: Filter? by map
        val armour_filters: Filter? by map
        val socket_filters: Filter? by map
        val req_filters: Filter? by map
        val map_filters: Filter? by map
        val misc_filters: Filter? by map
        val heist_filters: Filter? by map
        val trade_filters: Filter? by map
    }

    @JsonInclude
    data class Filter(
        var disabled: Boolean = true,
        @JsonSerialize(using = CustomSerializer::class)
        var filters: FilterFields? = null
    )

    data class FilterFields(
        val fields: List<FilterField>
    )

    data class FilterField(
        val name: String,
        val value: Any?
    )

    @JsonInclude(JsonInclude.Include.NON_NULL)
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

    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class MinMax(
        var min: Int? = null,
        var max: Int? = null
    ) {
        fun isEmpty() = listOfNotNull(min, max).isEmpty()
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class DropDown(
        var option: String? = null
    )

    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class Price(
        var min: Int? = null,
        var max: Int? = null,
        var option: String? = null
    ) {
        fun isEmpty() = listOfNotNull(min, max, option).isEmpty()
    }
}