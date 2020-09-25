package com.poetradeapp.fragments.item

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.poetradeapp.R
import com.example.poetradeapp.models.Enums
import kotlinx.android.synthetic.main.fragment_item_exchange_filters.*

class DropDownAdapter(
    context: Context,
    private val resId: Int,
    items: List<Enums.ItemCategory>
) : ArrayAdapter<Enums.ItemCategory>(context, resId, items) {

    private val suggestions = ArrayList<Enums.ItemCategory>()
    private val itemsAll = ArrayList(items)

    private val filter = object : Filter() {
        override fun convertResultToString(resultValue: Any?): CharSequence {
            return (resultValue as Enums.ItemCategory).text
        }

        override fun performFiltering(search: CharSequence?): FilterResults {
            return if (search != null) {
                suggestions.clear()
                itemsAll.forEach {
                    if (it.text.toLowerCase().contains(search.toString().toLowerCase())) {
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
                val filteredList = results.values as ArrayList<Enums.ItemCategory>
                clear()
                filteredList.forEach {
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
        view.findViewById<TextView>(R.id.itemText).text = item?.text
        return view
    }
}

class ItemExchangeFiltersFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_item_exchange_filters, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val itemCategoryItems = ArrayList<String>()
        Enums.ItemCategory.values().forEach {
            itemCategoryItems.add(it.text)
        }
        val adapter = DropDownAdapter(
            view.context,
            R.layout.dropdown_item,
            Enums.ItemCategory.values().toList()
        )
        itemCategoryDropDown.setAdapter(adapter)
        itemCategoryDropDown.setText("Any", true)
        itemCategoryDropDown.onItemClickListener =
            AdapterView.OnItemClickListener { p0, p1, p2, p3 ->
                println(p0?.getItemAtPosition(p2).toString())
            }
    }
}