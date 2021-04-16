package com.sgorinov.exilehelper.exchange.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sgorinov.exilehelper.exchange.R
import com.sgorinov.exilehelper.exchange.data.models.Filter
import com.sgorinov.exilehelper.exchange.presentation.models.enums.IBindableFieldViewHolder
import com.sgorinov.exilehelper.exchange.presentation.models.enums.IFilter
import com.sgorinov.exilehelper.exchange.presentation.models.enums.ViewType
import com.sgorinov.exilehelper.exchange.presentation.viewholders.*

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
                    R.layout.dropdown_view,
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
                    R.layout.account_view,
                    parent,
                    false
                )
                AccountViewHolder(view)
            }
            ViewType.Buyout.ordinal -> {
                val view = layoutInflater.inflate(
                    R.layout.buyout_view,
                    parent,
                    false
                )
                BuyoutViewHolder(view)
            }
            else -> {
                val view = layoutInflater.inflate(
                    R.layout.dropdown_view,
                    parent,
                    false
                )
                DropDownViewHolder(view)
            }
        }
    }
}