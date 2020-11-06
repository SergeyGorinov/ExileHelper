package com.poetradeapp.adapters

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.poetradeapp.R
import com.poetradeapp.models.enums.ItemDataType
import com.poetradeapp.models.enums.PropertyDisplayType
import com.poetradeapp.models.response.Property

class ItemTextDataAdapter(private val items: List<Pair<ItemDataType, Any?>>) :
    RecyclerView.Adapter<ItemTextViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemTextViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.items_result_info_group, parent, false)

        return ItemTextViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemTextViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}

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