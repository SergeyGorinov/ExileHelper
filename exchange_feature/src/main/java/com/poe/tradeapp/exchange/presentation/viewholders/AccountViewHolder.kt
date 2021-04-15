package com.poe.tradeapp.exchange.presentation.viewholders

import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.poe.tradeapp.exchange.data.models.Filter
import com.poe.tradeapp.exchange.databinding.AccountViewBinding
import com.poe.tradeapp.exchange.presentation.models.enums.IBindableFieldViewHolder
import com.poe.tradeapp.exchange.presentation.models.enums.IFilter

internal class AccountViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    IBindableFieldViewHolder {

    private val viewBinding = AccountViewBinding.bind(itemView)

    override fun bind(item: IFilter, filter: Filter) {
        val fieldId = item.id ?: return
        val field = filter.getField(fieldId)

        viewBinding.filterName.text = item.text
        viewBinding.filterAccount.doOnTextChanged { text, _, _, _ ->
            field.value = if (text.isNullOrBlank()) null else text.toString()
        }
        if (field.value != null) {
            viewBinding.filterAccount.setText(field.value.toString())
        }
    }
}