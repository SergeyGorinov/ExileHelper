package com.poe.tradeapp.exchange.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.poe.tradeapp.exchange.R
import com.poe.tradeapp.exchange.presentation.models.FetchedItem
import com.poe.tradeapp.exchange.presentation.viewholders.ItemsResultViewHolder

class ItemsResultAdapter : RecyclerView.Adapter<ItemsResultViewHolder>() {

    private val diffUtilCallback = object : DiffUtil.ItemCallback<FetchedItem>() {
        override fun areItemsTheSame(oldItem: FetchedItem, newItem: FetchedItem): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: FetchedItem, newItem: FetchedItem): Boolean {
            return oldItem == newItem
        }
    }

    private val asyncDiffer = AsyncListDiffer(this, diffUtilCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsResultViewHolder {
        val viewLayout =
            if (viewType == 0) R.layout.items_result_loading else R.layout.items_result_item
        val view = LayoutInflater.from(parent.context).inflate(viewLayout, parent, false)
        return ItemsResultViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemsResultViewHolder, position: Int) {
        val item = asyncDiffer.currentList.getOrNull(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    override fun getItemCount() = asyncDiffer.currentList.size

    override fun getItemViewType(position: Int) =
        if (asyncDiffer.currentList.getOrNull(position) == null) 0 else 1

    fun addLoader() {
        asyncDiffer.currentList.add(null)
        notifyItemInserted(asyncDiffer.currentList.size - 1)
    }

    fun addFetchedItems(fetchedItems: List<FetchedItem>) {
        asyncDiffer.currentList.remove(null)
        asyncDiffer.currentList.addAll(fetchedItems)
        notifyItemRangeChanged(asyncDiffer.currentList.size, fetchedItems.size)
        notifyDataSetChanged()
    }
}