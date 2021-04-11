package com.poe.tradeapp.exchange.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.poe.tradeapp.core.presentation.models.NotificationRequestViewData
import com.poe.tradeapp.exchange.R
import com.poe.tradeapp.exchange.databinding.NotificationItemBinding
import com.squareup.picasso.Picasso

class NotificationRequestsAdapter(private val items: List<NotificationRequestViewData>) :
    RecyclerView.Adapter<NotificationRequestsAdapter.NotificationRequestsViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationRequestsViewHolder {
        return NotificationRequestsViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.notification_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: NotificationRequestsViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    class NotificationRequestsViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private val viewBinding = NotificationItemBinding.bind(itemView)

        fun bind(item: NotificationRequestViewData) {
            val payingItemText = "${item.payingItemAmount} ${item.payingItemText}"
            viewBinding.buyingItemText.text = item.buyingItemText
            viewBinding.payingItemText.text = payingItemText
            Picasso.get().load(item.buyingItemImage).into(viewBinding.buyingItemImage)
            Picasso.get().load(item.payingItemImage).into(viewBinding.payingItemImage)
        }
    }
}