package com.sgorinov.exilehelper.exchange.presentation.viewholders

import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.sgorinov.exilehelper.exchange.data.models.Filter
import com.sgorinov.exilehelper.exchange.data.models.ItemsRequestModelFields
import com.sgorinov.exilehelper.exchange.databinding.MinmaxViewBinding
import com.sgorinov.exilehelper.exchange.presentation.models.enums.IBindableFieldViewHolder
import com.sgorinov.exilehelper.exchange.presentation.models.enums.IFilter

internal class MinMaxViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    IBindableFieldViewHolder {

    private val viewBinding = MinmaxViewBinding.bind(itemView)

    override fun bind(item: IFilter, filter: Filter) {
        val fieldId = item.id ?: return
        val field = filter.getOrCreateField(fieldId)

        restoreState(field.value as? ItemsRequestModelFields.MinMax)

        viewBinding.filterName.text = item.text
        viewBinding.filterMin.doOnTextChanged { _, _, _, _ ->
            val min = viewBinding.filterMin.text?.toString()?.toIntOrNull()
            val max = viewBinding.filterMax.text?.toString()?.toIntOrNull()
            val value = ItemsRequestModelFields.MinMax(min, max)
            field.value = if (value.isEmpty()) null else value
        }
        viewBinding.filterMax.doOnTextChanged { _, _, _, _ ->
            val min = viewBinding.filterMin.text?.toString()?.toIntOrNull()
            val max = viewBinding.filterMax.text?.toString()?.toIntOrNull()
            val value = ItemsRequestModelFields.MinMax(min, max)
            field.value = if (value.isEmpty()) null else value
        }
        if (field.value != null) {
            val value = field.value as ItemsRequestModelFields.MinMax
            if (value.min != null) {
                viewBinding.filterMin.setText(value.min.toString())
            }
            if (value.max != null) {
                viewBinding.filterMax.setText(value.max.toString())
            }
        }
    }

    private fun restoreState(field: ItemsRequestModelFields.MinMax?) {
        field ?: return
        viewBinding.filterMin.setText(field.min?.toString())
        viewBinding.filterMax.setText(field.max?.toString())
    }
}