package com.poetradeapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import com.poetradeapp.models.GenericEnum
import java.util.*
import kotlin.collections.ArrayList

class DropDownAdapter(
    context: Context,
    private val resId: Int,
    private val tvId: Int,
    items: List<GenericEnum>
) : ArrayAdapter<GenericEnum>(context, resId, tvId, items) {

    internal val suggestions = ArrayList<GenericEnum>()
    internal val itemsAll = ArrayList(items)

    private val filter = object : Filter() {
        override fun convertResultToString(resultValue: Any?): CharSequence {
            return (resultValue as GenericEnum).text
        }

        override fun performFiltering(search: CharSequence?): FilterResults {
            return if (search != null) {
                suggestions.clear()
                itemsAll.forEach {
                    if ((it as GenericEnum).text.toLowerCase(Locale.getDefault())
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
                val filteredList = results.values as ArrayList<*>
                clear()
                filteredList.forEach {
                    if (it is GenericEnum)
                        add(it)
                }
                notifyDataSetChanged()
            }
        }
    }

    override fun getFilter() = filter

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(resId, parent, false)
        val item = getItem(position)
        view.findViewById<TextView>(tvId).text = item?.text
        return view
    }
}