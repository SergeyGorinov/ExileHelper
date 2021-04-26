package com.sgorinov.exilehelper.exchange_feature.presentation.viewholders

import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.sgorinov.exilehelper.exchange_feature.data.models.LocalFilter
import com.sgorinov.exilehelper.exchange_feature.data.models.MinMax
import com.sgorinov.exilehelper.exchange_feature.databinding.MinmaxViewBinding
import com.sgorinov.exilehelper.exchange_feature.presentation.models.enums.IBindableFieldViewHolder
import com.sgorinov.exilehelper.exchange_feature.presentation.models.enums.IFilter

internal class MinMaxViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    IBindableFieldViewHolder {

    private val viewBinding = MinmaxViewBinding.bind(itemView)

    override fun bind(item: IFilter, localFilter: LocalFilter) {
        val fieldId = item.id ?: return
        val field = localFilter.getOrCreateField(fieldId)

        restoreState(field.value as? MinMax)

        viewBinding.filterName.text = item.text
        viewBinding.filterMin.doOnTextChanged { _, _, _, _ ->
            val min = viewBinding.filterMin.text?.toString()?.toIntOrNull()
            val max = viewBinding.filterMax.text?.toString()?.toIntOrNull()
            val value = MinMax(min, max)
            field.value = if (value.isEmpty()) null else value
        }
        viewBinding.filterMax.doOnTextChanged { _, _, _, _ ->
            val min = viewBinding.filterMin.text?.toString()?.toIntOrNull()
            val max = viewBinding.filterMax.text?.toString()?.toIntOrNull()
            val value = MinMax(min, max)
            field.value = if (value.isEmpty()) null else value
        }
        if (field.value != null) {
            val value = field.value as MinMax
            if (value.min != null) {
                viewBinding.filterMin.setText(value.min.toString())
            }
            if (value.max != null) {
                viewBinding.filterMax.setText(value.max.toString())
            }
        }
    }

    private fun restoreState(field: MinMax?) {
        field ?: return
        viewBinding.filterMin.setText(field.min?.toString())
        viewBinding.filterMax.setText(field.max?.toString())
    }
}