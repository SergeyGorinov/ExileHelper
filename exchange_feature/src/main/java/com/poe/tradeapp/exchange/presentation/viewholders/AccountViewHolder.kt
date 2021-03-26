package com.poe.tradeapp.exchange.presentation.viewholders

import android.view.View
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.poe.tradeapp.exchange.R
import com.poe.tradeapp.exchange.presentation.models.Filter
import com.poe.tradeapp.exchange.presentation.models.enums.IFilter

class AccountViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    IBindableFieldViewHolder {
    private val filterName: TextView = itemView.findViewById(R.id.filterName)
    private val filterAccount: TextInputEditText = itemView.findViewById(R.id.filterAccount)

    override fun bind(item: IFilter, filter: Filter) {
        val field = filter.getField(item.id ?: "")
        filterName.text = item.text
        filterAccount.doOnTextChanged { text, _, _, _ ->
            field.value = if (text.isNullOrBlank()) null else text.toString()
        }
        if (field.value != null)
            filterAccount.setText(field.value.toString())
    }
}