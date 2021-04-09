package com.poe.tradeapp.currency.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.poe.tradeapp.currency.R
import com.poe.tradeapp.currency.databinding.CurrencyGroupBinding
import com.poe.tradeapp.currency.presentation.models.CurrencyGroupViewData
import com.squareup.picasso.Picasso

internal class CurrencyGroupsAdapter(
    private val items: List<CurrencyGroupViewData>,
    private val onButtonClick: (String) -> Unit
) : RecyclerView.Adapter<CurrencyGroupsAdapter.CurrencyGroupsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CurrencyGroupsViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.currency_group, parent, false)
    )

    override fun onBindViewHolder(holder: CurrencyGroupsViewHolder, position: Int) {
        holder.bind(items[position], onButtonClick)
    }

    override fun getItemCount() = items.size

    internal class CurrencyGroupsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val viewBinding = CurrencyGroupBinding.bind(itemView)

        fun bind(group: CurrencyGroupViewData, onButtonClick: (String) -> Unit) {
            viewBinding.text.text = group.label
            viewBinding.root.setOnClickListener {
                onButtonClick(group.id)
            }
            Picasso.get().load(group.staticItems.firstOrNull()?.imageUrl).into(viewBinding.image)
        }
    }
}