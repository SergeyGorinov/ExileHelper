package com.sgorinov.exilehelper.exchange.presentation.viewholders

import android.view.View
import android.widget.AutoCompleteTextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.sgorinov.exilehelper.exchange.R
import com.sgorinov.exilehelper.exchange.data.models.Filter
import com.sgorinov.exilehelper.exchange.data.models.ItemsRequestModelFields
import com.sgorinov.exilehelper.exchange.databinding.DropdownViewBinding
import com.sgorinov.exilehelper.exchange.presentation.adapters.DropDownAdapter
import com.sgorinov.exilehelper.exchange.presentation.models.enums.IBindableFieldViewHolder
import com.sgorinov.exilehelper.exchange.presentation.models.enums.IEnum
import com.sgorinov.exilehelper.exchange.presentation.models.enums.IFilter

internal class DropDownViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    IBindableFieldViewHolder {

    private val viewBinding = DropdownViewBinding.bind(itemView)
    private val textFont = ResourcesCompat.getFont(
        itemView.context,
        com.sgorinov.exilehelper.core.R.font.fontinsmallcaps
    )

    override fun bind(item: IFilter, filter: Filter) {
        val field = filter.getOrCreateField(item.id ?: "")
        val adapter = DropDownAdapter(
            itemView.context,
            R.layout.dropdown_item,
            item.dropDownValues ?: listOf()
        )
        viewBinding.filterDropDown.typeface = textFont
        viewBinding.filterName.text = item.text
        viewBinding.filterDropDown.setAdapter(adapter)

        val selectedOption =
            (field.value as? ItemsRequestModelFields.DropDown)?.option?.let { selectedOption ->
                item.dropDownValues?.firstOrNull { it.id.equals(selectedOption, true) }
            }

        if (selectedOption != null) {
            adapter.selectedItem = selectedOption
            viewBinding.filterDropDown.setText(selectedOption.text, false)
        } else {
            viewBinding.filterDropDown.setText((item.dropDownValues?.firstOrNull())?.text, false)
        }

        viewBinding.filterDropDown.setOnItemClickListener { adapterView, _, position, _ ->
            val value = adapterView.getItemAtPosition(position) as IEnum?
            field.value =
                if (value?.id != null) ItemsRequestModelFields.DropDown(value.id) else null
            adapter.selectedItem = value
        }
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
    }
}