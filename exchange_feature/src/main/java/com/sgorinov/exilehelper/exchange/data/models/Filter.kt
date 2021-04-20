package com.sgorinov.exilehelper.exchange.data.models

import com.sgorinov.exilehelper.exchange.presentation.models.enums.ViewFilters
import com.sgorinov.exilehelper.exchange.presentation.models.enums.ViewType
import kotlinx.serialization.*
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.serialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder.Companion.DECODE_DONE
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure
import kotlin.properties.Delegates

@Serializable
internal class Filter(
    val name: String,
    @Transient
    var onFieldsChanged: (Boolean) -> Unit = {}
) {

    @Required
    var isEnabled: Boolean = false

    val fields: MutableList<Field> = mutableListOf()

    private val isFieldsEmpty
        get() = fields.isEmpty() || fields.all { it.value == null }

    fun getOrCreateField(id: String): Field {
        var field = fields.singleOrNull { s -> s.name == id }
        if (field == null) {
            field = Field(id)
            addField(field)
        }
        field.onValueChange = {
            onFieldsChanged(isFieldsEmpty)
        }
        return field
    }

    fun cleanFilter() {
        fields.clear()
        onFieldsChanged(isFieldsEmpty)
    }

    private fun addField(field: Field) {
        fields.add(field)
        onFieldsChanged(isFieldsEmpty)
    }
}

@Serializable(with = FieldSerializer::class)
internal data class Field(
    val name: String,
    @Transient
    var onValueChange: () -> Unit = {}
) {
    var value by Delegates.observable<Any?>(null) { _, _, _ ->
        onValueChange()
    }
}

internal object FieldSerializer : KSerializer<Field> {

    @Suppress("UNCHECKED_CAST")
    private val anyDataTypeSerializers = mapOf(
        "String" to String.serializer(),
        "Sockets" to ItemsRequestModelFields.Sockets.serializer(),
        "MinMax" to ItemsRequestModelFields.MinMax.serializer(),
        "DropDown" to ItemsRequestModelFields.DropDown.serializer(),
        "Price" to ItemsRequestModelFields.Price.serializer()
    ).mapValues { (_, v) ->
        v as KSerializer<Any>
    }

    override val descriptor = buildClassSerialDescriptor("Field") {
        element("name", serialDescriptor<String>())
        element("value", buildClassSerialDescriptor("Any"))
    }

    override fun deserialize(decoder: Decoder): Field {
        return decoder.decodeStructure(descriptor) {
            var fieldName = ""
            var fieldValue: Any? = null
            loop@ while (true) {
                when (decodeElementIndex(descriptor)) {
                    DECODE_DONE -> break@loop
                    0 -> fieldName = decodeStringElement(descriptor, 0)
                    1 -> fieldValue = decodeNullableSerializableElement(
                        descriptor,
                        1,
                        getValueSerializer(fieldName) as DeserializationStrategy<Any?>
                    )
                }
            }

            Field(fieldName).apply { value = fieldValue }
        }
    }

    override fun serialize(encoder: Encoder, value: Field) {
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, value.name)
            encodeNullableSerializableElement(
                descriptor,
                1,
                getValueSerializer(value.name),
                value.value
            )
        }
    }

    private fun getValueSerializer(fieldName: String): KSerializer<Any> {
        val filterType = ViewFilters.allFilters.flatMap {
            it.values
        }.firstOrNull {
            it.id == fieldName
        }
        val dataType = when (filterType?.viewType) {
            ViewType.Account -> "String"
            ViewType.Dropdown -> "DropDown"
            ViewType.Buyout -> "Price"
            ViewType.Minmax -> "MinMax"
            ViewType.Socket -> "Sockets"
            else -> ""
        }
        return anyDataTypeSerializers[dataType] ?: throw SerializationException(
            "Serializer for class $dataType is not registered in AnySerializer"
        )
    }
}