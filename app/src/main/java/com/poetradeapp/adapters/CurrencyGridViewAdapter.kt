package com.poetradeapp.adapters

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.poetradeapp.MainActivity
import com.example.poetradeapp.R
import com.google.android.material.button.MaterialButton
import com.poetradeapp.http.RequestService
import kotlinx.coroutines.*
import java.lang.Exception

class CurrencyGridViewAdapter(
    private val items: List<MainActivity.StaticData>,
    private val inflanter: LayoutInflater
) : BaseAdapter() {

    private val client = RequestService.create("https://www.pathofexile.com/")

    override fun getCount() = items.size

    override fun getItem(index: Int) = items[index]

    override fun getItemId(index: Int) = index.toLong()

    override fun getView(index: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: inflanter.inflate(R.layout.currency_button, null)
        val holder = Holder(view.findViewById(R.id.currencyButton))

        GlobalScope.launch {
            val image = items[index].image?.let { client.getStaticImage(it).execute().body()?.bytes() }
            withContext(Dispatchers.Main) {
                image?.let { holder.button.icon = BitmapDrawable(parent?.resources, BitmapFactory.decodeByteArray(it, 0, it.size)) }
            }
        }

        holder.button.setOnClickListener {
            println("Clicked on $index position")
        }
        return view
    }

    class Holder(val button: MaterialButton)
}