package com.poetradeapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import com.poetradeapp.models.enums.IEnum
import java.util.*
import kotlin.collections.ArrayList

class DropDownAdapter(
    context: Context,
    private val resId: Int,
    items: List<Any?>
) : ArrayAdapter<Any?>(context, resId, items) {

    internal val suggestions = ArrayList<IEnum>()
    internal val itemsAll = ArrayList(items)

    var selectedItem: IEnum? = items.first() as IEnum?

    private val filter = object : Filter() {
        override fun convertResultToString(resultValue: Any?): CharSequence {
            return (resultValue as IEnum).text
        }

        override fun performFiltering(search: CharSequence?): FilterResults {
            return if (search != null) {
                suggestions.clear()
                itemsAll.forEach {
                    if ((it as IEnum).text.toLowerCase(Locale.getDefault())
                            .contains(search.toString().toLowerCase(Locale.getDefault()))
                    ) {
                        suggestions.add(it)
                    }
                }
                val results = FilterResults()
                results.values = suggestions
                results.count = suggestions.size
                results
            } else {
                FilterResults()
            }
        }

        override fun publishResults(p0: CharSequence?, results: FilterResults?) {
            if (results != null && results.count > 0) {
                clear()
                addAll(results.values as ArrayList<*>)
                notifyDataSetChanged()
            }
        }
    }

    override fun getFilter() = filter

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view =
            (convertView ?: LayoutInflater.from(context).inflate(resId, parent, false)) as TextView
        val item = getItem(position)
        view.text = (item as IEnum?)?.text
        return view
    }
}