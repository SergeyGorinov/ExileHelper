package com.example.poetradeapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.TextView

class CustomSpinnerAdapter (private var leagueList: Array<MainActivity.League>, private val context: Context) : BaseAdapter(), AdapterView.OnItemSelectedListener {

    private val inflanter: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getItem(position: Int): MainActivity.League {
        return leagueList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return leagueList.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: inflanter.inflate(R.layout.custom_spinner_item, parent, false)
        val item: MainActivity.League = leagueList[position]
        view.findViewById<TextView>(R.id.LeagueListItem).text = item.text
        return view
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        //Toast.makeText(context, leagueList[position].id, Toast.LENGTH_SHORT).show()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        return
    }
}