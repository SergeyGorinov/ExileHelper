package com.poetradeapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.poetradeapp.MainActivity
import com.example.poetradeapp.R
import com.poetradeapp.RealmCurrencyData
import com.poetradeapp.models.MainViewModel

class CurrencyGroupAdapter(
    private val items: List<RealmCurrencyData>,
    private val fromWant: Boolean
) :
    RecyclerView.Adapter<CurrencyGroupViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyGroupViewHolder {

        val context = parent.context as MainActivity

        val model = ViewModelProvider(
            context,
            ViewModelProvider.AndroidViewModelFactory(context.application)
        ).get(MainViewModel::class.java)

        val view =
            LayoutInflater
                .from(context)
                .inflate(R.layout.currency_button, parent, false)

        return CurrencyGroupViewHolder(view, model, fromWant)
    }

    override fun onBindViewHolder(holder: CurrencyGroupViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}

class CurrencyGroupViewHolder(
    itemView: View,
    private val model: MainViewModel,
    private val fromWant: Boolean
) :
    RecyclerView.ViewHolder(itemView) {

    private val button: ImageButton = itemView.findViewById(R.id.currencyButton)
    private var item: RealmCurrencyData? = null

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

    fun bind(item: RealmCurrencyData) {
        if (this.item == null)
            this.item = item

        this.item?.let {
            val id = it.id
            button.setImageDrawable(model.getCurrencyIcon(id))
        }
    }
}