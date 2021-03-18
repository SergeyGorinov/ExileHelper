package com.poe.tradeapp.exchange.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.poe.tradeapp.exchange.R
import com.poe.tradeapp.exchange.presentation.models.enums.ItemDataType

class ItemTextDataAdapter(private val items: List<Pair<ItemDataType, Any?>>) :
    RecyclerView.Adapter<ItemTextViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemTextViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.items_result_info_group, parent, false)

        return ItemTextViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemTextViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}