package com.sgorinov.exilehelper.exchange.presentation.viewholders

import android.view.View
import android.widget.AutoCompleteTextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.sgorinov.exilehelper.exchange.R
import com.sgorinov.exilehelper.exchange.data.models.Filter
import com.sgorinov.exilehelper.exchange.data.models.ItemsRequestModelFields
import com.sgorinov.exilehelper.exchange.databinding.BuyoutViewBinding
import com.sgorinov.exilehelper.exchange.presentation.adapters.DropDownAdapter
import com.sgorinov.exilehelper.exchange.presentation.models.enums.IBindableFieldViewHolder
import com.sgorinov.exilehelper.exchange.presentation.models.enums.IEnum
import com.sgorinov.exilehelper.exchange.presentation.models.enums.IFilter

internal class BuyoutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    IBindableFieldViewHolder {

    private val viewBinding = BuyoutViewBinding.bind(itemView)
    private val textFont = ResourcesCompat.getFont(
        itemView.context,
        com.sgorinov.exilehelper.core.R.font.fontinsmallcaps
    )

    private var selectedItem: IEnum? = null

    override fun bind(item: IFilter, filter: Filter) {
        val fieldId = item.id ?: return
        val field = filter.getOrCreateField(fieldId)
        val adapter = DropDownAdapter(
            itemView.context,
            R.layout.dropdown_item,
            item.dropDownValues ?: listOf()
        )

        restoreState(field.value as? ItemsRequestModelFields.Price, item, adapter)

        viewBinding.filterDropDown.typeface = textFont
        viewBinding.filterName.text = item.text
        viewBinding.filterDropDown.setAdapter(adapter)

        viewBinding.filterDropDown.setText((item.dropDownValues?.firstOrNull())?.text, false)
        viewBinding.filterDropDown.setOnFocusChangeListener { view, focused ->
            if (view is AutoCompleteTextView) {
                if (focused) {
                    view.hint = adapter.selectedItem?.text
                    view.setText("", false)
                } else {
                    view.setText(adapter.selectedItem?.text, false)
                }
            }
        }
        viewBinding.filterDropDown.setOnItemClickListener { adapterView, _, position, _ ->
            selectedItem = adapterView.getItemAtPosition(position) as IEnum?
            val min = viewBinding.minValue.text?.toString()?.toIntOrNull()
            val max = viewBinding.maxValue.text?.toString()?.toIntOrNull()
            val value = ItemsRequestModelFields.Price(min, max, selectedItem?.id)
            field.value = if (value.isEmpty()) null else value
            adapter.selectedItem = selectedItem
        }

        viewBinding.minValue.doOnTextChanged { _, _, _, _ ->
            val min = viewBinding.minValue.text?.toString()?.toIntOrNull()
            val max = viewBinding.maxValue.text?.toString()?.toIntOrNull()
            val value = ItemsRequestModelFields.Price(min, max, selectedItem?.id)
            field.value = if (value.isEmpty()) null else value
        }
        viewBinding.maxValue.doOnTextChanged { _, _, _, _ ->
            val min = viewBinding.minValue.text?.toString()?.toIntOrNull()
            val max = viewBinding.maxValue.text?.toString()?.toIntOrNull()
            val value = ItemsRequestModelFields.Price(min, max, selectedItem?.id)
            field.value = if (value.isEmpty()) null else value
        }
        if (field.value != null) {
            val value = field.value as ItemsRequestModelFields.Price
            if (!value.option.isNullOrBlank()) {
                val valueText =
                    (item.dropDownValues?.singleOrNull { s -> s.id == value.option }) as IEnum
                viewBinding.filterDropDown.setText(valueText.text, false)
            }
            if (value.min != null) {
                viewBinding.minValue.setText(value.min.toString())
            }
            if (value.max != null) {
                viewBinding.maxValue.setText(value.max.toString())
            }
        }
    }

    private fun restoreState(
        field: ItemsRequestModelFields.Price?,
        item: IFilter,
        adapter: DropDownAdapter
    ) {
        field ?: return
        val selectedOption = field.option?.let { selectedOption ->
            item.dropDownValues?.firstOrNull { it.id.equals(selectedOption, true) }
        }

        if (selectedOption != null) {
            selectedItem = selectedOption
            adapter.selectedItem = selectedOption
            viewBinding.filterDropDown.setText(selectedOption.text, false)
        } else {
            viewBinding.filterDropDown.setText((item.dropDownValues?.firstOrNull())?.text, false)
        }
        viewBinding.minValue.setText(field.min?.toString())
        viewBinding.maxValue.setText(field.max?.toString())
    }
}