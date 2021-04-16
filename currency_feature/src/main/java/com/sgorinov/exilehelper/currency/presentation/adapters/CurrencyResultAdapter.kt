package com.sgorinov.exilehelper.currency.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sgorinov.exilehelper.currency.R
import com.sgorinov.exilehelper.currency.databinding.CurrencyResultItemBinding
import com.sgorinov.exilehelper.currency.presentation.models.CurrencyResultViewItem
import com.squareup.picasso.Picasso

internal class CurrencyResultAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val diffUtilCallback = object : DiffUtil.ItemCallback<CurrencyResultViewItem?>() {
        override fun areItemsTheSame(
            oldItem: CurrencyResultViewItem,
            newItem: CurrencyResultViewItem
        ): Boolean {
            return oldItem.accountName == newItem.accountName && oldItem.pay == newItem.pay
        }

        override fun areContentsTheSame(
            oldItem: CurrencyResultViewItem,
            newItem: CurrencyResultViewItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    private val asyncDiffer = AsyncListDiffer(this, diffUtilCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == 0) {
            LoaderViewHolder(inflater.inflate(R.layout.result_loading, parent, false))
        } else {
            CurrencyResultViewHolder(inflater.inflate(R.layout.currency_result_item, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = asyncDiffer.currentList.getOrNull(position)
        if (item != null && holder is CurrencyResultViewHolder) {
            holder.bind(item, position % 2 == 0)
        }
    }

    override fun getItemCount() = asyncDiffer.currentList.size

    override fun getItemViewType(position: Int) =
        if (asyncDiffer.currentList.getOrNull(position) == null) 0 else 1

    fun addData(data: List<CurrencyResultViewItem>) {
        asyncDiffer.submitList(data)
    }

    fun addLoader() {
        if (!asyncDiffer.currentList.contains(null)) {
            val currentList = asyncDiffer.currentList + null
            asyncDiffer.submitList(currentList)
        }
    }

    private class CurrencyResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val viewBinding = CurrencyResultItemBinding.bind(itemView)

        fun bind(item: CurrencyResultViewItem, oddRow: Boolean) {
            val color = ContextCompat.getColor(
                itemView.context, if (oddRow) {
                    R.color.odd_result_row_color
                } else {
                    R.color.even_result_row_color
                }
            )
            viewBinding.root.setBackgroundColor(color)
            viewBinding.stockText.text = itemView.context.getString(R.string.stock_text, item.stock)
            viewBinding.nickWithCountry.text = itemView.context.getString(
                R.string.account_text,
                item.accountName,
                item.lastCharacterName
            )
            viewBinding.playerStatus.text = item.status
            viewBinding.getExchangeText.text = itemView.context.getString(
                R.string.exchange_get_text,
                item.get
            )
            viewBinding.payExchangeText.text = itemView.context.getString(
                R.string.exchange_pay_text,
                item.pay
            )
            viewBinding.getExchangeLabel.text = item.getLabel
            viewBinding.payExchangeLabel.text = item.payLabel
            Picasso.get().load(item.getIcon).into(viewBinding.getExchangeImage)
            Picasso.get().load(item.payIcon).into(viewBinding.payExchangeImage)
        }
    }

    private class LoaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}