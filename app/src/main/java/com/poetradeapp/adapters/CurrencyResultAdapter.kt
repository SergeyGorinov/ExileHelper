package com.poetradeapp.adapters

import android.graphics.Rect
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.request.ImageRequest
import com.poetradeapp.R
import com.poetradeapp.activities.CurrencyExchangeActivity
import com.poetradeapp.models.ExchangeCurrencyResponseModel
import com.poetradeapp.ui.CenteredImageSpan
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CurrencyResultAdapter(private val items: List<ExchangeCurrencyResponseModel>) :
    RecyclerView.Adapter<CurrencyResultViewHolder>() {

    private var oddRow = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyResultViewHolder {
        val activity = parent.context as CurrencyExchangeActivity
        val view = LayoutInflater.from(activity)
            .inflate(R.layout.currency_result_item, parent, false)
        val holder = CurrencyResultViewHolder(view, activity, oddRow)
        oddRow = !oddRow
        return holder
    }

    override fun onBindViewHolder(holder: CurrencyResultViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}

class CurrencyResultViewHolder(
    itemView: View,
    activity: CurrencyExchangeActivity,
    oddRow: Boolean
) :
    RecyclerView.ViewHolder(itemView) {
    private val stockTextView = itemView.findViewById<TextView>(R.id.stockText)
    private val exchangeTextView = itemView.findViewById<TextView>(R.id.exchangeText)
    private val playerName = itemView.findViewById<TextView>(R.id.nickWithCountry)
    private val playerStatus = itemView.findViewById<TextView>(R.id.playerStatus)
    private val imageLoader = activity.imageLoader
    private val staticDataInstance = activity.staticDataInstance

    init {
        if (oddRow) {
            itemView.setBackgroundColor(itemView.context.getColor(R.color.odd_result_row_color))
        }
    }

    fun bind(item: ExchangeCurrencyResponseModel) {

        stockTextView.text =
            itemView.context.getString(R.string.stock_text, item.listing.price.item.stock)
        playerName.text = itemView.context.getString(
            R.string.account_text,
            item.listing.account.name,
            item.listing.account.lastCharacterName
        )
        playerStatus.text = item.listing.account.online?.status ?: "Online"

        val exchangeText = SpannableStringBuilder(
            itemView.context.getString(
                R.string.exchange_text,
                item.listing.price.item.amount,
                item.listing.price.exchange.amount
            )
        )

        val haveCurrency = staticDataInstance
            .getCurrencyData()
            .flatMap { f -> f.currencies }
            .firstOrNull { fon -> fon.id == item.listing.price.exchange.currency }
        val wantCurrency = staticDataInstance
            .getCurrencyData()
            .flatMap { f -> f.currencies }
            .firstOrNull { fon -> fon.id == item.listing.price.item.currency }

        GlobalScope.launch(Dispatchers.Default) {
            if (wantCurrency?.image != null) {
                val request = ImageRequest.Builder(itemView.context)
                    .data("https://www.pathofexile.com${wantCurrency.image}")
                    .size(32, 32)
                    .build()
                imageLoader.getImageLoader().execute(request).drawable?.let {
                    it.bounds =
                        Rect(
                            0,
                            0,
                            it.intrinsicWidth,
                            it.intrinsicHeight
                        )
                    exchangeText.setSpan(
                        CenteredImageSpan(it),
                        exchangeText.indexOf("  x") + 1,
                        exchangeText.indexOf("  x") + 2,
                        Spannable.SPAN_INCLUSIVE_EXCLUSIVE
                    )
                }
            } else {
                exchangeText.insert(exchangeText.indexOf("  x") + 1, wantCurrency?.label)
            }
            if (haveCurrency?.image != null) {
                val request = ImageRequest.Builder(itemView.context)
                    .data("https://www.pathofexile.com${haveCurrency.image}")
                    .size(32, 32)
                    .build()
                imageLoader.getImageLoader().execute(request).drawable?.let {
                    it.bounds =
                        Rect(
                            0,
                            0,
                            it.intrinsicWidth,
                            it.intrinsicHeight
                        )
                    exchangeText.setSpan(
                        CenteredImageSpan(it),
                        exchangeText.indexOf("x  ") + 2,
                        exchangeText.indexOf("x  ") + 3,
                        Spannable.SPAN_INCLUSIVE_EXCLUSIVE
                    )
                }
            } else {
                exchangeText.insert(exchangeText.indexOf("x  ") + 2, haveCurrency?.label)
            }
        }.invokeOnCompletion {
            GlobalScope.launch(Dispatchers.Main) {
                exchangeTextView.setText(exchangeText, TextView.BufferType.SPANNABLE)
            }
        }
    }
}