package com.poe.tradeapp.exchange.presentation

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import com.poe.tradeapp.exchange.R
import com.poe.tradeapp.exchange.presentation.models.Influences
import com.poe.tradeapp.exchange.presentation.models.ItemResultViewData
import com.poe.tradeapp.exchange.presentation.models.Property
import com.poe.tradeapp.exchange.presentation.models.enums.PropertyDisplayType

internal object SeparatorHelper {

    fun getSeparator(frameType: Int?): Int {
        return when (frameType) {
            0 -> R.drawable.seperator_normal
            1 -> R.drawable.seperator_magic
            2 -> R.drawable.separator_rare
            3 -> R.drawable.seperator_unique
            4 -> R.drawable.seperator_gem
            5 -> R.drawable.seperator_currency
            else -> R.drawable.seperator_normal
        }
    }

    fun getInfluenceIcons(item: ItemResultViewData): List<Int?> {
        return when {
            !item.influences.isEmpty() -> {
                item.influences.getInfluenceIcons()
            }
            item.synthesised ?: false -> {
                listOf(R.drawable.synthetic_symbol)
            }
            item.replica ?: false -> {
                listOf(R.drawable.experimented_symbol)
            }
            else -> listOf()
        }
    }

    fun getSpannableTextGroup(
        properties: List<Property>?,
        separator: String
    ): SpannableStringBuilder? {
        val spannableProperties = SpannableStringBuilder()
        properties?.joinTo(spannableProperties, separator = separator,
            transform = { getSpannableText(it) }
        )
        return if (spannableProperties.isBlank()) null else spannableProperties
    }

    private fun getSpannableText(property: Property): SpannableStringBuilder {
        val propertyGroupSpan = SpannableStringBuilder()
        when (property.displayMode) {
            PropertyDisplayType.NameValues.ordinal -> {
                propertyGroupSpan.append(property.name)
                if (property.values.isNotEmpty()) {
                    propertyGroupSpan.append(": ")
                    property.values.joinTo(propertyGroupSpan, transform = { prop ->
                        val spannableProp = SpannableString(prop.propertyValue)
                        spannableProp.setSpan(
                            ForegroundColorSpan(getPropertyColor(prop.propertyColor)),
                            0,
                            prop.propertyValue.length,
                            Spannable.SPAN_INCLUSIVE_EXCLUSIVE
                        )
                        spannableProp
                    })
                }
            }
            PropertyDisplayType.ValuesName.ordinal -> {
                if (property.values.isNotEmpty()) {
                    property.values.joinTo(propertyGroupSpan, transform = { prop ->
                        val spannableProp = SpannableString(prop.propertyValue)
                        spannableProp.setSpan(
                            ForegroundColorSpan(getPropertyColor(prop.propertyColor)),
                            0,
                            prop.propertyValue.length,
                            Spannable.SPAN_INCLUSIVE_EXCLUSIVE
                        )
                        spannableProp
                    })
                    propertyGroupSpan.append(' ')
                }
                propertyGroupSpan.append(property.name)
            }
            PropertyDisplayType.Placeholder.ordinal -> {
                propertyGroupSpan.append(property.name)
                if (property.values.isNotEmpty()) {
                    for (i in property.values.indices) {
                        val spannableProp = property.values[i].propertyValue
                        val pattern = "{$i}"
                        val replaceIndexPos = propertyGroupSpan.indexOf(pattern)
                        propertyGroupSpan.delete(
                            replaceIndexPos,
                            replaceIndexPos + pattern.length
                        )
                        propertyGroupSpan.insert(replaceIndexPos, spannableProp)
                        propertyGroupSpan.setSpan(
                            ForegroundColorSpan(getPropertyColor(property.values[i].propertyColor)),
                            replaceIndexPos,
                            replaceIndexPos + property.values[i].propertyValue.length,
                            Spannable.SPAN_INCLUSIVE_EXCLUSIVE
                        )
                    }
                }
            }
        }
        return propertyGroupSpan
    }

    private fun getPropertyColor(colorType: Int): Int {
        return when (colorType) {
            1 -> Color.rgb(0x88, 0x88, 0xFF)
            4 -> Color.rgb(0x96, 0x00, 0x00)
            5 -> Color.rgb(0x36, 0x64, 0x92)
            6 -> Color.rgb(0xFF, 0xD7, 0x00)
            7 -> Color.rgb(0xD0, 0x20, 0x90)
            else -> Color.WHITE
        }
    }

    private fun Influences.getInfluenceIcons(): List<Int?> {
        val influences = this.getInfluences()
        return when {
            influences.size > 1 -> {
                listOf(
                    influences[0].getDrawableIdByInfluenceName(),
                    influences[1].getDrawableIdByInfluenceName()
                )
            }
            influences.size == 1 -> {
                listOf(influences[0].getDrawableIdByInfluenceName())
            }
            else -> listOf()
        }
    }

    private fun String.getDrawableIdByInfluenceName(): Int? {
        return when (this) {
            "shaper" -> R.drawable.shaper_symbol
            "elder" -> R.drawable.elder_symbol
            "crusader" -> R.drawable.crusader_symbol
            "redeemer" -> R.drawable.redeemer_symbol
            "hunter" -> R.drawable.hunter_symbol
            "warlord" -> R.drawable.warlord_symbol
            else -> null
        }
    }
}