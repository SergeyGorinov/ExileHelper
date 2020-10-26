package com.poetradeapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import coil.request.ImageRequest
import com.poetradeapp.R
import com.poetradeapp.activities.CurrencyExchangeActivity
import com.poetradeapp.models.viewmodels.CurrencyExchangeViewModel
import com.poetradeapp.models.viewmodels.StaticItemViewData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class CurrencyGroupAdapter(
    private val items: List<StaticItemViewData>,
    private val fromWant: Boolean
) :
    RecyclerView.Adapter<CurrencyGroupViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyGroupViewHolder {

        val context = parent.context as CurrencyExchangeActivity

        val viewModel = ViewModelProvider(
            context,
            ViewModelProvider.AndroidViewModelFactory(context.application)
        ).get(CurrencyExchangeViewModel::class.java)

        val view =
            LayoutInflater
                .from(context)
                .inflate(R.layout.currency_button, parent, false)

        return CurrencyGroupViewHolder(view, viewModel, fromWant)
    }

    override fun onBindViewHolder(holder: CurrencyGroupViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}

@ExperimentalCoroutinesApi
class CurrencyGroupViewHolder(
    itemView: View,
    private val viewModel: CurrencyExchangeViewModel,
    private val fromWant: Boolean
) :
    RecyclerView.ViewHolder(itemView) {

    private val button: ImageButton = itemView.findViewById(R.id.currencyButton)
    private val imageLoader = (itemView.context as CurrencyExchangeActivity).imageLoader

    fun bind(item: StaticItemViewData) {

        button.setOnClickListener { button ->
            button.isSelected = !button.isSelected
            if (button.isSelected) {
                if (fromWant) viewModel.addWantCurrency(item.id)
                else viewModel.addHaveCurrency(item.id)
            } else {
                if (fromWant) viewModel.removeWantCurrency(item.id)
                else viewModel.removeHaveCurrency(item.id)
            }
        }

        if (item.drawable == null) {
            item.image?.let { link ->
                GlobalScope.launch(Dispatchers.Main) {
                    val request = ImageRequest.Builder(itemView.context)
                        .data("https://www.pathofexile.com$link")
                        .size(32, 32)
                        .build()
                    item.drawable = imageLoader.execute(request).drawable
                }.invokeOnCompletion {
                    GlobalScope.launch(Dispatchers.Main) {
                        button.setImageDrawable(item.drawable)
                    }
                }
            }
        } else {
            button.setImageDrawable(item.drawable)
        }
    }
}