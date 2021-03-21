package com.poe.tradeapp.charts_feature.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.poe.tradeapp.charts_feature.R
import com.poe.tradeapp.charts_feature.databinding.ItemGroupBinding
import com.poe.tradeapp.charts_feature.presentation.models.ItemGroup
import com.squareup.picasso.Picasso

internal class ItemsGroupsAdapter(
    private val items: List<ItemGroup>,
    private val onButtonClick: (String, Boolean) -> Unit
) : RecyclerView.Adapter<ItemsGroupsAdapter.CurrencyGroupsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CurrencyGroupsViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_group, parent, false)
    )

    override fun onBindViewHolder(holder: CurrencyGroupsViewHolder, position: Int) {
        holder.bind(items[position], onButtonClick)
    }

    override fun getItemCount() = items.size

    internal class CurrencyGroupsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val viewBinding = ItemGroupBinding.bind(itemView)

        fun bind(group: ItemGroup, onButtonClick: (String, Boolean) -> Unit) {
            viewBinding.text.text = group.label
            viewBinding.root.setOnClickListener {
                onButtonClick(group.type, group.isCurrency)
            }
            Picasso.get().load(group.iconUrl).into(viewBinding.image)
        }
    }
}