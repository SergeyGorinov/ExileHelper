package com.example.poetradeapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StaticListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var StaticImage : ImageView? = itemView.findViewById(R.id.StaticItemImage)
    var StaticId: TextView? = itemView.findViewById(R.id.StaticItemId)
    var StaticText: TextView? = itemView.findViewById(R.id.StaticItemText)
}

class CustomListViewAdapter(private val staticItemsList: Array<MainActivity.ViewModel>, private val context: Context) : RecyclerView.Adapter<StaticListViewHolder>() {

    override fun getItemCount(): Int {
        return staticItemsList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StaticListViewHolder {
        var itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.custom_static_item, parent, false)
        return StaticListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: StaticListViewHolder, position: Int) {
        val item: MainActivity.ViewModel = staticItemsList[position]
        val viewHolder: StaticListViewHolder = holder

        viewHolder.StaticId!!.text = item.id
        viewHolder.StaticText!!.text = item.text
        viewHolder.StaticImage!!.setImageBitmap(item.image)
    }
}