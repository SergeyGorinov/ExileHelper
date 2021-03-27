package com.poe.tradeapp.exchange.presentation.viewholders

import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.poe.tradeapp.exchange.R
import com.poe.tradeapp.exchange.data.models.ItemsRequestModelFields
import com.poe.tradeapp.exchange.presentation.adapters.DropDownAdapter
import com.poe.tradeapp.exchange.presentation.models.Filter
import com.poe.tradeapp.exchange.presentation.models.enums.IEnum
import com.poe.tradeapp.exchange.presentation.models.enums.IFilter

internal class DropDownViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    IBindableFieldViewHolder {

    private val filterName: TextView = itemView.findViewById(R.id.filterName)
    private val filterDropDown: AutoCompleteTextView = itemView.findViewById(R.id.filterDropDown)
    private val textFont = ResourcesCompat.getFont(
        itemView.context,
        com.poe.tradeapp.core.R.font.fontinsmallcaps
    )

    override fun bind(item: IFilter, filter: Filter) {
        val field = filter.getField(item.id ?: "")

        filterDropDown.typeface = textFont

        filterName.text = item.text

        filterDropDown.setAdapter(
            DropDownAdapter(
                itemView.context,
                R.layout.dropdown_item,
                item.dropDownValues?.toList() ?: listOf()
            )
        )

        filterDropDown.setText((item.dropDownValues?.first() as IEnum?)?.text, false)

        filterDropDown.setOnItemClickListener { adapterView, _, position, _ ->
            val value = adapterView.getItemAtPosition(position) as IEnum?
            val adapter = adapterView.adapter
            field.value =
                if (value?.id != null) ItemsRequestModelFields.DropDown(value.id) else null
            if (adapter is DropDownAdapter)
                adapter.selectedItem = value
        }

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

        if (field.value != null) {
            val currentValue = field.value as ItemsRequestModelFields.DropDown
            val value =
                (item.dropDownValues?.singleOrNull { s -> (s as IEnum).id == currentValue.option }) as IEnum
            filterDropDown.setText(value.text, false)
        }
    }
}