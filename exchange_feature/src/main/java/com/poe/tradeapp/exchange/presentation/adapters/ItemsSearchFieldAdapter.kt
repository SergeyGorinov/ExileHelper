package com.poe.tradeapp.exchange.presentation.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.poe.tradeapp.exchange.R
import com.poe.tradeapp.exchange.presentation.models.SearchableItem
import java.util.*

@Suppress("UNCHECKED_CAST")
class ItemsSearchFieldAdapter(
    context: Context,
    resId: Int,
    private val allItems: List<Void>,
    private val items: ArrayList<SearchableItem> = arrayListOf()
) : ArrayAdapter<SearchableItem>(context, resId, items) {

    var selectedItem: SearchableItem? = null

    private val suggestions: ArrayList<SearchableItem> = arrayListOf()

    override fun getItem(position: Int) = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = items.size

    override fun getFilter() = customFilter

    private val customFilter = object : Filter() {
        @SuppressLint("SyntheticAccessor")
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filterResults = FilterResults()
            if (!constraint.isNullOrBlank()) {
                suggestions.clear()
                val normalizedConstraint = constraint.toString().toLowerCase(Locale.getDefault())
                allItems.forEach { group ->
                    val filtered = arrayListOf<SearchableItem>()
//                    group.items.forEach { item ->
//                        if (item.text.toLowerCase(Locale.getDefault())
//                                .contains(normalizedConstraint)
//                        )
//                            filtered.add(SearchableItem(false, item.text, item.type, item.name))
//                    }
//                    if (filtered.isNotEmpty()) {
//                        suggestions.add(SearchableItem(true, group.group.label, group.group.label))
//                        suggestions.addAll(filtered)
//                    }
                }
                filterResults.values = suggestions
                filterResults.count = suggestions.size
            }
            return filterResults
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            if (results != null && results.count > 0) {
                clear()
                addAll(results.values as ArrayList<SearchableItem>)
            }
        }

        override fun convertResultToString(resultValue: Any?): CharSequence {
            return if (resultValue is SearchableItem)
                resultValue.text
            else
                ""
        }
    }

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
}