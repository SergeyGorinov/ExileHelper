package com.poe.tradeapp.exchange.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.poe.tradeapp.exchange.R
import com.poe.tradeapp.exchange.presentation.models.Filter
import com.poe.tradeapp.exchange.presentation.models.enums.IFilter
import com.poe.tradeapp.exchange.presentation.models.enums.ViewType
import com.poe.tradeapp.exchange.presentation.viewholders.*

internal class ItemsFilterAdapter(private val items: Array<*>, private val filter: Filter) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        getViewHolderByType(viewType, LayoutInflater.from(parent.context), parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is IBindableFieldViewHolder) {
            holder.bind(items[position] as IFilter, filter)
        }
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int) =
        (items[position] as IFilter).viewType.ordinal

    override fun getItemId(position: Int) = items[position].hashCode().toLong()

    private fun getViewHolderByType(
        viewType: Int,
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            ViewType.Dropdown.ordinal -> {
                val view = layoutInflater.inflate(
                    R.layout.filters_dropdown_item,
                    parent,
                    false
                )
                DropDownViewHolder(view)
            }
            ViewType.Minmax.ordinal -> {
                val view = layoutInflater.inflate(
                    R.layout.filters_minmax_item,
                    parent,
                    false
                )
                MinMaxViewHolder(view)
            }
            ViewType.Socket.ordinal -> {
                val view = layoutInflater.inflate(
                    R.layout.filters_socket_item,
                    parent,
                    false
                )
                SocketViewHolder(view)
            }
            ViewType.Account.ordinal -> {
                val view = layoutInflater.inflate(
                    R.layout.filters_account_item,
                    parent,
                    false
                )
                AccountViewHolder(view)
            }
            ViewType.Buyout.ordinal -> {
                val view = layoutInflater.inflate(
                    R.layout.filters_buyout_item,
                    parent,
                    false
                )
                BuyoutViewHolder(view)
            }
            else -> {
                val view = layoutInflater.inflate(
                    R.layout.filters_dropdown_item,
                    parent,
                    false
                )
                DropDownViewHolder(view)
            }
        }
    }
}