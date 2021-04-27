package com.sgorinov.exilehelper.currency_feature.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sgorinov.exilehelper.core.presentation.models.NotificationRequestViewData
import com.sgorinov.exilehelper.currency_feature.R
import com.sgorinov.exilehelper.currency_feature.databinding.NotificationItemBinding
import com.squareup.picasso.Picasso

class NotificationRequestsAdapter(private val items: MutableList<NotificationRequestViewData>) :
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

    fun deleteItem(position: Int): NotificationRequestViewData {
        val item = items[position]
        items.remove(item)
        notifyItemRemoved(position)
        return item
    }

    fun restoreItem(position: Int, item: NotificationRequestViewData) {
        items.add(position, item)
        notifyItemInserted(position)
    }

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