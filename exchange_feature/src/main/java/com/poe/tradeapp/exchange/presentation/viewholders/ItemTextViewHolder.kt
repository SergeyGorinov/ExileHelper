package com.poe.tradeapp.exchange.presentation.viewholders

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.poe.tradeapp.exchange.R
import com.poe.tradeapp.exchange.data.models.Property
import com.poe.tradeapp.exchange.presentation.models.enums.ItemDataType
import com.poe.tradeapp.exchange.presentation.models.enums.PropertyDisplayType

class ItemTextViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val groupText = itemView.findViewById<TextView>(R.id.groupText)

    fun bind(property: Pair<ItemDataType, Any?>) {
        val propertyText = SpannableStringBuilder()

        val properties = property.second as List<*>
        if (properties.first() is String) {
            propertyText.append(
                properties.joinToString(
                    separator = "\n",
                    transform = { t -> t.toString() }
                )
            )
        } else {
            for (i in properties.indices - 1) {
                val currentProperty = properties[i] as Property
                when (currentProperty.displayMode) {
                    PropertyDisplayType.NameValues.ordinal -> {
                        propertyText.append(currentProperty.name)

                        if (currentProperty.values.isNotEmpty()) {
                            propertyText.append(": ")

                            val colorizedProperties = currentProperty.values.map {
                                val colorizedProperty = SpannableString(it.first)
                                colorizedProperty.setSpan(
                                    ForegroundColorSpan(Color.YELLOW),
                                    0,
                                    it.first.length,
                                    Spannable.SPAN_INCLUSIVE_EXCLUSIVE
                                )
                                colorizedProperty
                            }

                            propertyText.append(colorizedProperties.joinToString())
                        }

                        propertyText.append("\n")
                    }
                    PropertyDisplayType.ValuesName.ordinal -> {
                        propertyText.append(currentProperty.values[0].first)
                        propertyText.append(' ')
                        propertyText.append(currentProperty.name)
                        propertyText.append("\n")
                    }
                    PropertyDisplayType.Progressbar.ordinal -> {

                    }
                    PropertyDisplayType.Placeholder.ordinal -> {

                    }
                }
            }
            val lastProperty = properties.last() as Property
            when (lastProperty.displayMode) {
                PropertyDisplayType.NameValues.ordinal -> {
                    propertyText.append(lastProperty.name)

                    if (lastProperty.values.isNotEmpty()) {
                        propertyText.append(": ")

                        val colorizedProperties = lastProperty.values.map {
                            val colorizedProperty = SpannableString(it.first)
                            colorizedProperty.setSpan(
                                ForegroundColorSpan(Color.YELLOW),
                                0,
                                it.first.length,
                                Spannable.SPAN_INCLUSIVE_EXCLUSIVE
                            )
                            colorizedProperty
                        }

                        propertyText.append(colorizedProperties.joinToString())
                    }
                }
                PropertyDisplayType.ValuesName.ordinal -> {
                    propertyText.append(lastProperty.values[0].first)
                    propertyText.append(' ')
                    propertyText.append(lastProperty.name)
                }
                PropertyDisplayType.Progressbar.ordinal -> {

                }
                PropertyDisplayType.Placeholder.ordinal -> {

                }
            }
        }

        groupText.text = propertyText
    }
}