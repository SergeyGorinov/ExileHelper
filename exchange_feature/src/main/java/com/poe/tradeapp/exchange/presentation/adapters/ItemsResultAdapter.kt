package com.poe.tradeapp.exchange.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.poe.tradeapp.exchange.R
import com.poe.tradeapp.exchange.presentation.ItemsSearchActivity
import com.poe.tradeapp.exchange.presentation.models.FetchedItem
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class ItemsResultAdapter(initItems: List<FetchedItem>) :
    RecyclerView.Adapter<ItemsResultViewHolder>() {

    private val items = arrayListOf<FetchedItem?>()

    init {
        items.addAll(initItems)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsResultViewHolder {
        val viewLayout =
            if (viewType == 0) R.layout.items_result_loading else R.layout.items_result_item
        val activity = parent.context as ItemsSearchActivity
        val view = LayoutInflater.from(activity).inflate(viewLayout, parent, false)
        return ItemsResultViewHolder(view, activity)
    }

    override fun onBindViewHolder(holder: ItemsResultViewHolder, position: Int) {
        val item = items.getOrNull(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int) =
        if (items.getOrNull(position) == null) 0 else 1

    fun addLoader() {
        items.add(null)
        notifyItemInserted(items.size - 1)
    }

    fun addFetchedItems(fetchedItems: List<FetchedItem>) {
        items.remove(null)
        items.addAll(fetchedItems)
        notifyItemRangeChanged(items.size, fetchedItems.size)
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int) = items.getOrNull(position).hashCode().toLong()
}