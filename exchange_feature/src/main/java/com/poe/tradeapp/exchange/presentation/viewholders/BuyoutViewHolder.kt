package com.poe.tradeapp.exchange.presentation.viewholders

import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.poe.tradeapp.exchange.R
import com.poe.tradeapp.exchange.data.models.ItemsRequestModelFields
import com.poe.tradeapp.exchange.presentation.adapters.DropDownAdapter
import com.poe.tradeapp.exchange.presentation.models.Filter
import com.poe.tradeapp.exchange.presentation.models.enums.IEnum
import com.poe.tradeapp.exchange.presentation.models.enums.IFilter

internal class BuyoutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    IBindableFieldViewHolder {
    private val filterName: TextView = itemView.findViewById(R.id.filterName)
    private val filterDropDown: AutoCompleteTextView = itemView.findViewById(R.id.filterDropDown)
    private val minValue: TextInputEditText = itemView.findViewById(R.id.minValue)
    private val maxValue: TextInputEditText = itemView.findViewById(R.id.maxValue)

    private var selectedItem: IEnum? = null

    override fun bind(item: IFilter, filter: Filter) {
        val field = filter.getField(item.id ?: "")

        filterName.text = item.text

        filterDropDown.setAdapter(
            DropDownAdapter(
                itemView.context,
                R.layout.dropdown_item,
                item.dropDownValues?.toList() ?: listOf()
            )
        )

        filterDropDown.setText((item.dropDownValues?.first() as IEnum?)?.text, false)

        filterDropDown.setOnFocusChangeListener { view, focused ->
            val adapter = (view as AutoCompleteTextView).adapter
            if (adapter is DropDownAdapter) {
                if (focused) {
                    view.hint = adapter.selectedItem?.text
                    view.setText("", false)
                } else {
                    view.setText(adapter.selectedItem?.text, false)
                }
            }
        }

        filterDropDown.setOnItemClickListener { adapterView, _, position, _ ->
            selectedItem = adapterView.getItemAtPosition(position) as IEnum?
            val adapter = adapterView.adapter
            val min = minValue.text?.toString()?.toIntOrNull()
            val max = maxValue.text?.toString()?.toIntOrNull()
            val value = ItemsRequestModelFields.Price(min, max, selectedItem?.id)
            field.value = if (value.isEmpty()) null else value
            if (adapter is DropDownAdapter)
                adapter.selectedItem = selectedItem
        }

        minValue.doOnTextChanged { _, _, _, _ ->
            val min = minValue.text?.toString()?.toIntOrNull()
            val max = maxValue.text?.toString()?.toIntOrNull()
            val value = ItemsRequestModelFields.Price(min, max, selectedItem?.id)
            field.value = if (value.isEmpty()) null else value
        }

        maxValue.doOnTextChanged { _, _, _, _ ->
            val min = minValue.text?.toString()?.toIntOrNull()
            val max = maxValue.text?.toString()?.toIntOrNull()
            val value = ItemsRequestModelFields.Price(min, max, selectedItem?.id)
            field.value = if (value.isEmpty()) null else value
        }

        if (field.value != null) {
            val value = field.value as ItemsRequestModelFields.Price
            if (!value.option.isNullOrBlank()) {
                val valueText =
                    (item.dropDownValues?.singleOrNull { s -> (s as IEnum).id == value.option }) as IEnum
                filterDropDown.setText(valueText.text, false)
            }
            if (value.min != null) {
                minValue.setText(value.min.toString())
            }
            if (value.max != null) {
                maxValue.setText(value.max.toString())
            }
        }
    }
}