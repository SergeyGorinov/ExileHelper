package com.poetradeapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.poetradeapp.R
import com.poetradeapp.MainActivity
import com.poetradeapp.models.ExchangeCurrencyResponseModel
import com.poetradeapp.models.MainViewModel

class CurrencyResultAdapter(private val items: List<ExchangeCurrencyResponseModel>) :
    RecyclerView.Adapter<CurrencyResultViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyResultViewHolder {
        val context = parent.context as MainActivity
        val view = LayoutInflater.from(context)
            .inflate(R.layout.currency_result_item, parent, false)
        val model = ViewModelProvider(
            context,
            ViewModelProvider.AndroidViewModelFactory(context.application)
        ).get(MainViewModel::class.java)
        return CurrencyResultViewHolder(view, model)
    }

    override fun onBindViewHolder(holder: CurrencyResultViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}

class CurrencyResultViewHolder(itemView: View, private val model: MainViewModel) :
    RecyclerView.ViewHolder(itemView) {
    private val haveImageRelativePrice: ImageView =
        itemView.findViewById(R.id.haveImageRelativePrice)
    private val wantImageRelativePrice: ImageView =
        itemView.findViewById(R.id.wantImageRelativePrice)
    private val havewantRelativePrice: TextView =
        itemView.findViewById(R.id.havewantRelativePrice)
    private val wantImageRelativePriceReverse: ImageView =
        itemView.findViewById(R.id.wantImageRelativePriceReverse)
    private val haveImageRelativePriceReverse: ImageView =
        itemView.findViewById(R.id.haveImageRelativePriceReverse)
    private val wanthaveRelativePrice: TextView =
        itemView.findViewById(R.id.wanthaveRelativePrice)

    private val wantLabelExchange: TextView =
        itemView.findViewById(R.id.wantLabelExchange)
    private val wantImageExchange: ImageView =
        itemView.findViewById(R.id.wantImageExchange)
    private val wantCountExchange: TextView =
        itemView.findViewById(R.id.wantCountExchange)
    private val haveLabelExchange: TextView =
        itemView.findViewById(R.id.haveLabelExchange)
    private val haveImageExchange: ImageView =
        itemView.findViewById(R.id.haveImageExchange)
    private val haveCountExchange: TextView =
        itemView.findViewById(R.id.haveCountExchange)

    private val stockCount: TextView =
        itemView.findViewById(R.id.stockCount)
    private val stockLabel: TextView =
        itemView.findViewById(R.id.stockLabel)
    private val account: TextView =
        itemView.findViewById(R.id.account)
    private val status: TextView =
        itemView.findViewById(R.id.status)

    fun bind(item: ExchangeCurrencyResponseModel) {
        val haveCurrency = model.getMainData().flatMap { f -> f.entries }
            .firstOrNull { fon -> fon.id == item.listing.price.exchange.currency }
        val wantCurrency = model.getMainData().flatMap { f -> f.entries }
            .firstOrNull { fon -> fon.id == item.listing.price.item.currency }

        val haveWantPrice =
            "%.4f".format((item.listing.price.item.amount / item.listing.price.exchange.amount).toFloat())
        val wantHavePrice =
            "%.4f".format((item.listing.price.exchange.amount / item.listing.price.item.amount).toFloat())

        val fullAccount = "${item.listing.account.name}(${item.listing.account.lastCharacterName})"

        haveCurrency?.drawable?.let {
            haveImageRelativePrice.setImageDrawable(it)
            haveImageRelativePriceReverse.setImageDrawable(it)
            haveImageExchange.setImageDrawable(it)
        }

        wantCurrency?.drawable?.let {
            wantImageRelativePrice.setImageDrawable(it)
            wantImageRelativePriceReverse.setImageDrawable(it)
            wantImageExchange.setImageDrawable(it)
        }

        havewantRelativePrice.text = haveWantPrice
        wanthaveRelativePrice.text = wantHavePrice

        haveLabelExchange.text = haveCurrency?.text ?: "Unknown"
        wantLabelExchange.text = wantCurrency?.text ?: "Unknown"

        haveCountExchange.text = item.listing.price.exchange.amount.toString()
        wantCountExchange.text = item.listing.price.item.amount.toString()

        stockCount.text = item.listing.price.item.stock.toString()
        stockLabel.text = wantCurrency?.text ?: "Unknown"
        account.text = fullAccount
        status.text = item.listing.account.online.status ?: "Online"
    }
}