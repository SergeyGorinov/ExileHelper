package com.poetradeapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.request.ImageRequest
import com.poetradeapp.R
import com.poetradeapp.activities.ItemsSearchActivity
import com.poetradeapp.models.ExchangeItemsResponseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ItemsResultAdapter(private val items: List<ExchangeItemsResponseModel>) :
    RecyclerView.Adapter<ItemsResultViewHolder>() {

    private var oddRow = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsResultViewHolder {
        val activity = parent.context as ItemsSearchActivity
        val view = LayoutInflater.from(activity)
            .inflate(R.layout.items_result_item, parent, false)
        val holder = ItemsResultViewHolder(view, activity, oddRow)
        oddRow = !oddRow
        return holder
    }

    override fun onBindViewHolder(holder: ItemsResultViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}

class ItemsResultViewHolder(
    itemView: View,
    activity: ItemsSearchActivity,
    oddRow: Boolean
) :
    RecyclerView.ViewHolder(itemView) {
    private val itemImageContainer = itemView.findViewById<FrameLayout>(R.id.itemImageContainer)
    private val itemImage = itemView.findViewById<ImageView>(R.id.itemImage)
    private val itemExpicitMods = itemView.findViewById<TextView>(R.id.itemExpicitMods)
    private val imageLoader = activity.imageLoader
    private val socketsTemplate = activity.socketsTemplate

    init {
        if (oddRow) {
            itemView.setBackgroundColor(itemView.context.getColor(R.color.odd_result_row_color))
        }
    }

    fun bind(item: ExchangeItemsResponseModel) {
        GlobalScope.launch(Dispatchers.Default) {
            val request = ImageRequest.Builder(itemView.context)
                .data(item.item.icon)
                .allowHardware(false)
                .build()
            val dpi = itemView.context.resources.displayMetrics.density
            imageLoader.getImageLoader().execute(request).drawable?.let {
                GlobalScope.launch(Dispatchers.Main) {
                    itemImage.minimumWidth = (it.intrinsicWidth * dpi).toInt()
                    itemImage.minimumHeight = (it.intrinsicHeight * dpi).toInt()
                    itemImage.setImageDrawable(it)
                }
            }
        }

        if (item.item.sockets != null) {
            itemImageContainer.addView(socketsTemplate.prepareTemplate(item.item.sockets))
        }

        if (item.item.explicitMods != null && item.item.explicitMods.isNotEmpty()) {
            itemExpicitMods.text = item.item.explicitMods.joinToString("\n")
        }
    }
}