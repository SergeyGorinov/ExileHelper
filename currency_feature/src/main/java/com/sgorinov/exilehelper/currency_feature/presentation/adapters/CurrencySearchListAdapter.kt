package com.sgorinov.exilehelper.currency_feature.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.sgorinov.exilehelper.core.presentation.toLowerCaseLocalized
import com.sgorinov.exilehelper.currency_feature.R
import com.sgorinov.exilehelper.currency_feature.presentation.models.CurrencyViewData
import com.squareup.picasso.Picasso
import java.util.concurrent.CopyOnWriteArrayList

internal class CurrencySearchListAdapter(
    context: Context,
    resId: Int,
    private val allItems: List<CurrencyViewData>,
    private val items: CopyOnWriteArrayList<CurrencyViewData> = CopyOnWriteArrayList()
) : ArrayAdapter<CurrencyViewData>(context, resId, items) {

    var selectedItem: CurrencyViewData? = null

    private var currentCount = 0

    private val suggestions: CopyOnWriteArrayList<CurrencyViewData> = CopyOnWriteArrayList()

    override fun getItem(position: Int): CurrencyViewData? = items.getOrNull(position)

    override fun getCount(): Int = currentCount

    override fun getFilter() = CustomFilter(suggestions, allItems, items, this)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val item = getItem(position)
        val view = (convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.dropdown_image_item, parent, false))
        item ?: return view
        val imageView = view.findViewById<AppCompatImageView>(R.id.image)
        val textView = view.findViewById<AppCompatTextView>(R.id.text)
        textView.text = item.label
        if (item.imageUrl != null) {
            Picasso.get().load(item.imageUrl).into(imageView)
        } else {
            imageView.visibility = View.GONE
        }
        return view
    }

    override fun notifyDataSetChanged() {
        currentCount = suggestions.size
        super.notifyDataSetChanged()
    }

    internal class CustomFilter(
        private val suggestions: CopyOnWriteArrayList<CurrencyViewData>,
        private val allItems: List<CurrencyViewData>,
        private val items: CopyOnWriteArrayList<CurrencyViewData>,
        private val adapter: CurrencySearchListAdapter
    ) : Filter() {

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            suggestions.clear()
            val filterResults = FilterResults()
            if (!constraint.isNullOrBlank()) {
                val normalizedConstraint = constraint.toString().toLowerCaseLocalized()
                val filteredItems = allItems.filter {
                    it.label.toLowerCaseLocalized().contains(normalizedConstraint)
                }
                if (filteredItems.isNotEmpty()) {
                    suggestions.addAll(filteredItems)
                } else {
                    adapter.notifyDataSetChanged()
                }
                filterResults.values = suggestions
                filterResults.count = suggestions.size
            }
            return filterResults
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            items.clear()
            if (results != null && results.count > 0) {
                (results.values as? List<*>)?.forEach {
                    if (it is CurrencyViewData) {
                        items.add(it)
                    }
                }
                adapter.notifyDataSetChanged()
            } else {
                adapter.notifyDataSetInvalidated()
            }
        }

        override fun convertResultToString(resultValue: Any?): CharSequence {
            return if (resultValue is CurrencyViewData)
                resultValue.label
            else
                ""
        }
    }
}