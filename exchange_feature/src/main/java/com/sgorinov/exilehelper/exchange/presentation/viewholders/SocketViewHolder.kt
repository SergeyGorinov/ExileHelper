package com.sgorinov.exilehelper.exchange.presentation.viewholders

import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.sgorinov.exilehelper.exchange.data.models.Filter
import com.sgorinov.exilehelper.exchange.data.models.ItemsRequestModelFields
import com.sgorinov.exilehelper.exchange.databinding.SocketsViewBinding
import com.sgorinov.exilehelper.exchange.presentation.models.enums.IBindableFieldViewHolder
import com.sgorinov.exilehelper.exchange.presentation.models.enums.IFilter

internal class SocketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    IBindableFieldViewHolder {

    private val viewBinding = SocketsViewBinding.bind(itemView)

    override fun bind(item: IFilter, filter: Filter) {
        val fieldId = item.id ?: return
        val field = filter.getOrCreateField(fieldId)
        viewBinding.filterName.text = item.text
        viewBinding.filterRed.doOnTextChanged { _, _, _, _ ->
            field.value = getSocketGroupData()
        }
        viewBinding.filterGreen.doOnTextChanged { _, _, _, _ ->
            field.value = getSocketGroupData()
        }
        viewBinding.filterBlue.doOnTextChanged { _, _, _, _ ->
            field.value = getSocketGroupData()
        }
        viewBinding.filterWhite.doOnTextChanged { _, _, _, _ ->
            field.value = getSocketGroupData()
        }
        viewBinding.filterMin.doOnTextChanged { _, _, _, _ ->
            field.value = getSocketGroupData()
        }
        viewBinding.filterMax.doOnTextChanged { _, _, _, _ ->
            field.value = getSocketGroupData()
        }
    }

    private fun getSocketGroupData(): ItemsRequestModelFields.Sockets? {
        val value = ItemsRequestModelFields.Sockets(
            viewBinding.filterRed.text?.toString()?.toIntOrNull(),
            viewBinding.filterGreen.text?.toString()?.toIntOrNull(),
            viewBinding.filterBlue.text?.toString()?.toIntOrNull(),
            viewBinding.filterWhite.text?.toString()?.toIntOrNull(),
            viewBinding.filterMin.text?.toString()?.toIntOrNull(),
            viewBinding.filterMax.text?.toString()?.toIntOrNull()
        )
        return if (value.isEmpty())
            null
        else
            value
    }
}