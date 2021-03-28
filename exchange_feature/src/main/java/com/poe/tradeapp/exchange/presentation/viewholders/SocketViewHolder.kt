package com.poe.tradeapp.exchange.presentation.viewholders

import android.view.View
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.poe.tradeapp.exchange.R
import com.poe.tradeapp.exchange.data.models.ItemsRequestModelFields
import com.poe.tradeapp.exchange.presentation.models.Filter
import com.poe.tradeapp.exchange.presentation.models.enums.IBindableFieldViewHolder
import com.poe.tradeapp.exchange.presentation.models.enums.IFilter

internal class SocketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    IBindableFieldViewHolder {
    private val filterName: TextView = itemView.findViewById(R.id.filterName)
    private val filterRed: TextInputEditText = itemView.findViewById(R.id.filterRed)
    private val filterGreen: TextInputEditText = itemView.findViewById(R.id.filterGreen)
    private val filterBlue: TextInputEditText = itemView.findViewById(R.id.filterBlue)
    private val filterWhite: TextInputEditText = itemView.findViewById(R.id.filterWhite)
    private val filterMin: TextInputEditText = itemView.findViewById(R.id.filterMin)
    private val filterMax: TextInputEditText = itemView.findViewById(R.id.filterMax)

    override fun bind(item: IFilter, filter: Filter) {
        val fieldId = item.id ?: return
        val field = filter.getField(fieldId)

        filterName.text = item.text

        filterRed.doOnTextChanged { _, _, _, _ ->
            field.value = getSocketGroupData()
        }
        filterGreen.doOnTextChanged { _, _, _, _ ->
            field.value = getSocketGroupData()
        }
        filterBlue.doOnTextChanged { _, _, _, _ ->
            field.value = getSocketGroupData()
        }
        filterWhite.doOnTextChanged { _, _, _, _ ->
            field.value = getSocketGroupData()
        }
        filterMin.doOnTextChanged { _, _, _, _ ->
            field.value = getSocketGroupData()
        }
        filterMax.doOnTextChanged { _, _, _, _ ->
            field.value = getSocketGroupData()
        }
    }

    private fun getSocketGroupData(): ItemsRequestModelFields.Sockets? {
        val value = ItemsRequestModelFields.Sockets(
            filterRed.text?.toString()?.toIntOrNull(),
            filterGreen.text?.toString()?.toIntOrNull(),
            filterBlue.text?.toString()?.toIntOrNull(),
            filterWhite.text?.toString()?.toIntOrNull(),
            filterMin.text?.toString()?.toIntOrNull(),
            filterMax.text?.toString()?.toIntOrNull()
        )
        return if (value.isEmpty())
            null
        else
            value
    }
}