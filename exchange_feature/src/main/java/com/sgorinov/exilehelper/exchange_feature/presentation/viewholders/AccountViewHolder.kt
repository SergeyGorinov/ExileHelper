package com.sgorinov.exilehelper.exchange_feature.presentation.viewholders

import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.sgorinov.exilehelper.exchange_feature.data.models.LocalFilter
import com.sgorinov.exilehelper.exchange_feature.databinding.AccountViewBinding
import com.sgorinov.exilehelper.exchange_feature.presentation.models.enums.IBindableFieldViewHolder
import com.sgorinov.exilehelper.exchange_feature.presentation.models.enums.IFilter

internal class AccountViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    IBindableFieldViewHolder {

    private val viewBinding = AccountViewBinding.bind(itemView)

    override fun bind(item: IFilter, localFilter: LocalFilter) {
        val fieldId = item.id ?: return
        val field = localFilter.getOrCreateField(fieldId)

        viewBinding.filterName.text = item.text
        viewBinding.filterAccount.doOnTextChanged { text, _, _, _ ->
            field.value = if (text.isNullOrBlank()) null else text.toString()
        }
        if (field.value != null) {
            viewBinding.filterAccount.setText(field.value.toString())
        }
    }
}