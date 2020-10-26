package com.poetradeapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.poetradeapp.R

class ItemsSearchFieldAdapter(
    context: Context,
    resId: Int,
    tvId: Int,
    private var items: List<Triple<Int, String, String?>>
) : ArrayAdapter<Triple<Int, String, String?>>(context, resId, tvId, items) {

    override fun getItem(position: Int): Triple<Int, String, String?>? = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = items.size

    override fun isEnabled(position: Int) = getItem(position)?.first != 0

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val item = getItem(position)
        val isHeader = item?.first == 0
        val itemLayout = if (isHeader) R.layout.dropdown_header_item else R.layout.dropdown_item
        val view = convertView ?: LayoutInflater.from(context).inflate(itemLayout, parent, false)
        if (isHeader)
            view.findViewById<TextView>(R.id.itemText).text = item?.second
        else
            view.findViewById<TextView>(R.id.itemText).text =
                if (item?.third != null) "${item.third} ${item.second}" else item?.second
        return view
    }
}