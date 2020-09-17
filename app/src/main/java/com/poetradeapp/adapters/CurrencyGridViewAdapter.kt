package com.poetradeapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import com.example.poetradeapp.R
import com.poetradeapp.http.RequestService
import com.poetradeapp.models.StaticData

class CurrencyGridViewAdapter(
    private val items: List<StaticData>,
    private val inflanter: LayoutInflater
) : BaseAdapter() {

    private val client = RequestService.create("https://www.pathofexile.com/")

    override fun getCount() = items.size

    override fun getItem(index: Int) = items[index]

    override fun getItemId(index: Int) = index.toLong()

    override fun getView(index: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: inflanter.inflate(R.layout.currency_button, null)
        val holder = Holder(view.findViewById(R.id.currencyButton))

        items[index].drawable?.let { holder.button.setImageDrawable(it) }

        holder.button.setOnClickListener {
            println("Clicked on $index position")
        }
        return view
    }

    class Holder(val button: ImageButton)
}