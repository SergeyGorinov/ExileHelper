package com.example.poetradeapp

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.net.HttpURLConnection
import java.net.URL

class CustomListViewAdapter(private var staticItemsList: Array<MainActivity.StaticData>, private val context: Context) : BaseAdapter() {

    private val inflanter: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getItem(position: Int): MainActivity.StaticData {
        return staticItemsList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return staticItemsList.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: inflanter.inflate(R.layout.custom_static_item, parent, false)
        val item: MainActivity.StaticData = staticItemsList[position]

        view.findViewById<TextView>(R.id.StaticItemId).text = item.id
        view.findViewById<TextView>(R.id.StaticItemText).text = item.text

        try {
            doAsync {
                val url = URL("https://www.pathofexile.com" + item.image)
                val connection = url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                val imageStream = connection.inputStream
                val image = BitmapFactory.decodeStream(imageStream)
                uiThread {
                    view.findViewById<ImageView>(R.id.StaticItemImage).setImageBitmap(image)
                }
            }
        }
        catch (e: Exception) {
            println(e)
        }
        return view
    }
}