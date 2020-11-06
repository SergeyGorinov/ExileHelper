package com.poetradeapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.poetradeapp.R
import com.poetradeapp.activities.CurrencyExchangeActivity
import com.poetradeapp.models.ui.StaticItemViewData
import com.poetradeapp.models.view.CurrencyExchangeViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class CardsGroupAdapter(
    private val fromWant: Boolean
) :
    RecyclerView.Adapter<CardsGroupViewHolder>() {

    private var items: List<StaticItemViewData> = listOf()

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardsGroupViewHolder {

        val context = parent.context as CurrencyExchangeActivity

        val model = ViewModelProvider(
            context,
            ViewModelProvider.AndroidViewModelFactory(context.application)
        ).get(CurrencyExchangeViewModel::class.java)

        val view =
            LayoutInflater
                .from(context)
                .inflate(R.layout.card_button, parent, false)

        return CardsGroupViewHolder(view, model, fromWant)
    }

    override fun onBindViewHolder(holder: CardsGroupViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun updateItems(items: List<StaticItemViewData>) {
        this.items = items
        notifyItemRangeChanged(0, items.size)
        notifyDataSetChanged()
    }
}

class CardsGroupViewHolder(
    itemView: View,
    private val model: CurrencyExchangeViewModel,
    private val fromWant: Boolean
) :
    RecyclerView.ViewHolder(itemView) {

    private val button: Button = itemView.findViewById(R.id.cardButton)
    private var item: StaticItemViewData? = null

    init {
        button.setOnClickListener { button ->
            button.isSelected = !button.isSelected
            item?.let {
                if (button.isSelected) {
                    if (fromWant) model.addWantCurrency(it.id)
                    else model.addHaveCurrency(it.id)
                } else {
                    if (fromWant) model.removeWantCurrency(it.id)
                    else model.removeHaveCurrency(it.id)
                }
            }
        }
    }

    fun bind(item: StaticItemViewData) {
        this.item = item

        this.item?.let {
            button.text = it.label
        }
    }
}