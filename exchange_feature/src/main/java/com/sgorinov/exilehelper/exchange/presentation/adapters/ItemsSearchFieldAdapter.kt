package com.sgorinov.exilehelper.exchange.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.sgorinov.exilehelper.core.presentation.toLowerCaseLocalized
import com.sgorinov.exilehelper.exchange.R
import com.sgorinov.exilehelper.exchange.presentation.models.ItemGroupViewData
import com.sgorinov.exilehelper.exchange.presentation.models.SuggestionItem
import java.util.concurrent.CopyOnWriteArrayList

internal class ItemsSearchFieldAdapter(
    context: Context,
    resId: Int,
    private val allItems: List<ItemGroupViewData>,
    private val items: CopyOnWriteArrayList<SuggestionItem> = CopyOnWriteArrayList()
) : ArrayAdapter<SuggestionItem>(context, resId, items) {

    var selectedItem: SuggestionItem? = null
    private var currentCount = 0

    private val suggestions: CopyOnWriteArrayList<SuggestionItem> = CopyOnWriteArrayList()

    override fun getItem(position: Int): SuggestionItem? = items.getOrNull(position)

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = currentCount

    override fun getFilter() = CustomFilter(suggestions, allItems, items, this)

    override fun isEnabled(position: Int) = getItemViewType(position) != 0

    override fun getItemViewType(position: Int) = if (getItem(position)?.isHeader == true) 0 else 1

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val item = getItem(position)
        val view = (convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.dropdown_item, parent, false)) as TextView
        item ?: return view
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

    override fun notifyDataSetChanged() {
        currentCount = suggestions.size
        super.notifyDataSetChanged()
    }

    internal class CustomFilter(
        private val suggestions: CopyOnWriteArrayList<SuggestionItem>,
        private val allItems: List<ItemGroupViewData>,
        private val items: CopyOnWriteArrayList<SuggestionItem>,
        private val adapter: ItemsSearchFieldAdapter
    ) : Filter() {

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            suggestions.clear()
            val filterResults = FilterResults()
            if (!constraint.isNullOrBlank()) {
                val normalizedConstraint = constraint.toString().toLowerCaseLocalized()
                val filteredItems = allItems.mapNotNull { group ->
                    val entries = group.entries.filter {
                        it.text.toLowerCaseLocalized().contains(normalizedConstraint)
                    }.map { SuggestionItem(false, it.text, it.type, it.name) }
                    if (entries.isNotEmpty()) {
                        listOf(SuggestionItem(true, group.label, group.label)) + entries
                    } else {
                        null
                    }
                }.flatten()
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
                    if (it is SuggestionItem) {
                        items.add(it)
                    }
                }
                adapter.notifyDataSetChanged()
            } else {
                adapter.notifyDataSetInvalidated()
            }
        }

        override fun convertResultToString(resultValue: Any?): CharSequence {
            return if (resultValue is SuggestionItem)
                resultValue.text
            else
                ""
        }
    }
}