package com.poe.tradeapp.exchange.presentation.viewholders

import android.view.View
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.poe.tradeapp.exchange.R
import com.poe.tradeapp.exchange.data.models.ItemsRequestModelFields
import com.poe.tradeapp.exchange.presentation.models.Filter
import com.poe.tradeapp.exchange.presentation.models.enums.IFilter

class MinMaxViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    IBindableFieldViewHolder {
    private val filterName: TextView = itemView.findViewById(R.id.filterName)
    private val filterMin: TextInputEditText = itemView.findViewById(R.id.filterMin)
    private val filterMax: TextInputEditText = itemView.findViewById(R.id.filterMax)

    override fun bind(item: IFilter, filter: Filter) {
        val field = filter.getField(item.id ?: "")

        filterName.text = item.text

        filterMin.doOnTextChanged { _, _, _, _ ->
            val min = filterMin.text?.toString()?.toIntOrNull()
            val max = filterMax.text?.toString()?.toIntOrNull()
            val value = ItemsRequestModelFields.MinMax(min, max)
            field.value = if (value.isEmpty()) null else value
        }

        filterMax.doOnTextChanged { _, _, _, _ ->
            val min = filterMin.text?.toString()?.toIntOrNull()
            val max = filterMax.text?.toString()?.toIntOrNull()
            val value = ItemsRequestModelFields.MinMax(min, max)
            field.value = if (value.isEmpty()) null else value
        }

        if (field.value != null) {
            val value = field.value as ItemsRequestModelFields.MinMax
            if (value.min != null) {
                filterMin.setText(value.min.toString())
            }
            if (value.max != null) {
                filterMax.setText(value.max.toString())
            }
        }
    }
}