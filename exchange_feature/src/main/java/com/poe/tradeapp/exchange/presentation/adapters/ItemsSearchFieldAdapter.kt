package com.poe.tradeapp.exchange.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.poe.tradeapp.core.presentation.toLowerCaseLocalized
import com.poe.tradeapp.exchange.R
import com.poe.tradeapp.exchange.presentation.models.ItemGroupViewData
import com.poe.tradeapp.exchange.presentation.models.ItemViewData
import com.poe.tradeapp.exchange.presentation.models.SearchableItem

internal class ItemsSearchFieldAdapter(
    context: Context,
    resId: Int,
    private val allItems: List<ItemGroupViewData>,
    private val items: ArrayList<SearchableItem> = arrayListOf()
) : ArrayAdapter<SearchableItem>(context, resId, items) {

    var selectedItem: SearchableItem? = null

    private val suggestions: ArrayList<SearchableItem> = arrayListOf()

    override fun getItem(position: Int) = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = items.size

    override fun getFilter() = CustomFilter(suggestions, allItems, items)

    override fun isEnabled(position: Int) = getItemViewType(position) != 0

    override fun getItemViewType(position: Int) = if (getItem(position).isHeader) 0 else 1

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val item = getItem(position)
        val view = (convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.dropdown_item, parent, false)) as TextView
        view.text = item.text
        val backgroundColor = if (getItemViewType(position) == 0)
            ContextCompat.getColor(
                context,
                R.color.items_search_suggestions_header
            ) else
            ContextCompat.getColor(
                context,
                R.color.primaryColor
            )
        val textColor = if (getItemViewType(position) == 0)
            ContextCompat.getColor(
                context,
                R.color.items_search_suggestions_header_text
            ) else
            ContextCompat.getColor(
                context,
                R.color.items_search_suggestions_item_text
            )
        view.setBackgroundColor(backgroundColor)
        view.setTextColor(textColor)
        return view
    }

    internal class CustomFilter(
        private val suggestions: ArrayList<SearchableItem>,
        private val allItems: List<ItemGroupViewData>,
        private val items: ArrayList<SearchableItem>
    ) : Filter() {

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filterResults = FilterResults()
            if (!constraint.isNullOrBlank()) {
                suggestions.clear()
                val normalizedConstraint = constraint.toString().toLowerCaseLocalized()
                allItems.forEach { group ->
                    val filteredItems = arrayListOf<SearchableItem>()
                    group.entries.forEach { item ->
                        if (item.text.toLowerCaseLocalized().contains(normalizedConstraint)) {
                            filteredItems.add(
                                SearchableItem(
                                    false,
                                    item.text,
                                    item.type,
                                    item.name
                                )
                            )
                        }
                    }
                    if (filteredItems.isNotEmpty()) {
                        suggestions.add(SearchableItem(true, group.label, group.label))
                        suggestions.addAll(filteredItems)
                    }
                }
                filterResults.values = suggestions
                filterResults.count = suggestions.size
            }
            return filterResults
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            if (results != null && results.count > 0) {
                items.clear()
                (results.values as? List<*>)?.forEach {
                    if (it is SearchableItem) {
                        items.add(it)
                    }
                }
            }
        }

        override fun convertResultToString(resultValue: Any?): CharSequence {
            return if (resultValue is ItemViewData)
                resultValue.text
            else
                ""
        }
    }
}