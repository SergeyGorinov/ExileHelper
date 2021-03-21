package com.poe.tradeapp.currency.presentation.adapters

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.poe.tradeapp.core.presentation.CenteredImageSpan
import com.poe.tradeapp.core.presentation.dp
import com.poe.tradeapp.currency.R
import com.poe.tradeapp.currency.databinding.CurrencyResultItemBinding
import com.poe.tradeapp.currency.presentation.models.CurrencyResultViewItem
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

internal class CurrencyResultAdapter(
    private val items: List<CurrencyResultViewItem>,
    private val onImageLoad: (Int) -> Unit
) : RecyclerView.Adapter<CurrencyResultAdapter.CurrencyResultViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyResultViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.currency_result_item, parent, false)
        return CurrencyResultViewHolder(view)
    }

    override fun onBindViewHolder(holder: CurrencyResultViewHolder, position: Int) {
        holder.bind(items[position], position % 2 == 0) {
            onImageLoad(position)
        }
    }

    override fun getItemCount() = items.size

    internal class CurrencyResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val viewBinding = CurrencyResultItemBinding.bind(itemView)

        fun bind(item: CurrencyResultViewItem, oddRow: Boolean, onImageLoad: () -> Unit) {
            val color = ContextCompat.getColor(
                itemView.context, if (oddRow) {
                    R.color.odd_result_row_color
                } else {
                    R.color.even_result_row_color
                }
            )
            val getExchangeText = SpannableStringBuilder(
                itemView.context.getString(
                    R.string.exchange_get_text,
                    item.get
                )
            )
            val payExchangeText = SpannableStringBuilder(
                itemView.context.getString(
                    R.string.exchange_pay_text,
                    item.pay
                )
            )
            insertCurrency(item.getIcon, item.getLabel, getExchangeText, onImageLoad)
            insertCurrency(item.payIcon, item.payLabel, payExchangeText, onImageLoad)
            viewBinding.root.setBackgroundColor(color)
            viewBinding.stockText.text = itemView.context.getString(R.string.stock_text, item.stock)
            viewBinding.nickWithCountry.text = itemView.context.getString(
                R.string.account_text,
                item.accountName,
                item.lastCharacterName
            )
            viewBinding.playerStatus.text = item.status
            viewBinding.getExchangeText.text = getExchangeText
            viewBinding.payExchangeText.text = payExchangeText
        }

        private fun insertCurrency(
            currencyIcon: String?,
            currencyLabel: String?,
            text: SpannableStringBuilder,
            onImageLoad: () -> Unit
        ) {
            when {
                currencyIcon != null -> {
                    val textLength = text.length
                    Picasso.get().load(currencyIcon).resize(24.dp, 24.dp).into(object : Target {
                        override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                            bitmap ?: return
                            val drawable = BitmapDrawable(itemView.resources, bitmap).apply {
                                setBounds(0, 0, intrinsicWidth, intrinsicHeight)
                            }
                            text.setSpan(
                                CenteredImageSpan(drawable),
                                textLength - 1,
                                textLength,
                                SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                            onImageLoad()
                        }

                        override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) = Unit

                        override fun onPrepareLoad(placeHolderDrawable: Drawable?) = Unit
                    })
                    text.append(" $currencyLabel")
                }
                currencyLabel != null -> {
                    text.replace(
                        text.length - 1,
                        text.length,
                        currencyLabel
                    )
                }
                else -> {
                    text.replace(
                        text.length - 1,
                        text.length,
                        "Unknown"
                    )
                }
            }
        }
    }
}