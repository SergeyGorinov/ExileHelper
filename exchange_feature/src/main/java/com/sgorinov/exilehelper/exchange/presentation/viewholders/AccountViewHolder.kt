package com.sgorinov.exilehelper.exchange.presentation.viewholders

import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.sgorinov.exilehelper.exchange.data.models.Filter
import com.sgorinov.exilehelper.exchange.databinding.AccountViewBinding
import com.sgorinov.exilehelper.exchange.presentation.models.enums.IBindableFieldViewHolder
import com.sgorinov.exilehelper.exchange.presentation.models.enums.IFilter

internal class AccountViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    IBindableFieldViewHolder {

    private val viewBinding = AccountViewBinding.bind(itemView)

    override fun bind(item: IFilter, filter: Filter) {
        val fieldId = item.id ?: return
        val field = filter.getOrCreateField(fieldId)

        viewBinding.filterName.text = item.text
        viewBinding.filterAccount.doOnTextChanged { text, _, _, _ ->
            field.value = if (text.isNullOrBlank()) null else text.toString()
        }
        if (field.value != null) {
            viewBinding.filterAccount.setText(field.value.toString())
        }
    }
}