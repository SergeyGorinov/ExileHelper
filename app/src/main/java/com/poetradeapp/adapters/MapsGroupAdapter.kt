package com.poetradeapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.poetradeapp.R
import com.poetradeapp.flexbox.FlexDirection
import com.poetradeapp.flexbox.FlexWrap
import com.poetradeapp.flexbox.FlexboxLayoutManager
import com.poetradeapp.flexbox.JustifyContent
import com.poetradeapp.models.viewmodels.StaticItemViewData
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Suppress("TYPE_INFERENCE_ONLY_INPUT_TYPES_WARNING")
class MapsGroupAdapter(
    private val fromWant: Boolean
) :
    RecyclerView.Adapter<MapsGroupViewHolder>() {

    private var items: Map<String?, List<StaticItemViewData>> = mapOf()

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MapsGroupViewHolder {
        val view =
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.map_group, parent, false)

        return MapsGroupViewHolder(view, fromWant, parent.context)
    }

    override fun onBindViewHolder(holder: MapsGroupViewHolder, position: Int) {
        val key = items.keys.elementAt(position)
        holder.bind(key, items[key] ?: listOf())
    }

    fun updateItems(items: Map<String?, List<StaticItemViewData>>) {
        this.items = items
        notifyItemRangeChanged(0, items.size)
        notifyDataSetChanged()
    }
}

@ExperimentalCoroutinesApi
class MapsGroupViewHolder(
    itemView: View,
    fromWant: Boolean,
    context: Context
) :
    RecyclerView.ViewHolder(itemView) {

    private val header: TextView = itemView.findViewById(R.id.mapGroupHeader)
    private val group: RecyclerView = itemView.findViewById(R.id.mapGroup)
    private val adapter = CardsGroupAdapter(fromWant)
    private val flexboxLayoutManager = FlexboxLayoutManager(context)

    init {
        flexboxLayoutManager.flexWrap = FlexWrap.WRAP
        flexboxLayoutManager.justifyContent = JustifyContent.CENTER
        flexboxLayoutManager.flexDirection = FlexDirection.ROW

        group.adapter = adapter
        group.layoutManager = flexboxLayoutManager
    }

    fun bind(groupName: String?, items: List<StaticItemViewData>) {
        header.text = groupName
        adapter.updateItems(items)
    }
}