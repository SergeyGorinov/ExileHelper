package com.poe.tradeapp.currency.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.poe.tradeapp.core.presentation.dp
import com.poe.tradeapp.currency.R
import com.poe.tradeapp.currency.databinding.CurrencySelectedItemBinding
import com.poe.tradeapp.currency.presentation.models.StaticItemViewData
import com.squareup.picasso.Picasso

internal class CurrencySelectedAdapter(
    private val items: List<StaticItemViewData>,
    private val onRemoveClick: (String) -> Unit
) : RecyclerView.Adapter<CurrencySelectedAdapter.CurrencySelectedViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencySelectedViewHolder {
        return CurrencySelectedViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.currency_selected_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CurrencySelectedViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item) {
            onRemoveClick(item.id)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount() = items.size

    internal class CurrencySelectedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val viewBinding = CurrencySelectedItemBinding.bind(itemView)

        fun bind(item: StaticItemViewData, onRemoveClick: () -> Unit) {
            if (item.imageUrl != null) {
                Picasso.get().load(item.imageUrl).resize(36.dp, 36.dp).into(viewBinding.image)
            }
            viewBinding.label.text = item.label
            viewBinding.remove.setOnClickListener {
                onRemoveClick()
            }
        }
    }
}