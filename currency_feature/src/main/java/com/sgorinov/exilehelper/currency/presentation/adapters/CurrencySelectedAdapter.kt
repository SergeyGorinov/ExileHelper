package com.sgorinov.exilehelper.currency.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sgorinov.exilehelper.currency.R
import com.sgorinov.exilehelper.currency.databinding.CurrencySelectedItemBinding
import com.sgorinov.exilehelper.currency.presentation.models.CurrencyViewData
import com.squareup.picasso.Picasso

internal class CurrencySelectedAdapter(private val onRemove: (String, Boolean) -> Unit) :
    RecyclerView.Adapter<CurrencySelectedAdapter.CurrencySelectedViewHolder>() {

    private var items: MutableList<CurrencyViewData> = mutableListOf()
    private var isWantList: Boolean = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencySelectedViewHolder {
        return CurrencySelectedViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.currency_selected_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CurrencySelectedViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    fun setItems(items: MutableList<CurrencyViewData>, isWantList: Boolean) {
        this.items = items
        this.isWantList = isWantList
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int) {
        val item = items[position]
        onRemove(item.id, isWantList)
        items.remove(item)
        notifyItemRemoved(position)
    }

    internal class CurrencySelectedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val viewBinding = CurrencySelectedItemBinding.bind(itemView)

        fun bind(item: CurrencyViewData) {
            if (item.imageUrl != null) {
                Picasso.get().load(item.imageUrl).resize(32, 32).into(viewBinding.image)
            }
            viewBinding.label.text = item.label
        }
    }
}