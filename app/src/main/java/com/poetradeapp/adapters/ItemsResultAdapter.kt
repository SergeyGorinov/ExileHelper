package com.poetradeapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.request.ImageRequest
import com.poetradeapp.R
import com.poetradeapp.activities.ItemsSearchActivity
import com.poetradeapp.models.ui.FetchedItem
import com.poetradeapp.utility.Helpers.Companion.getSeparator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class ItemsResultAdapter(initItems: List<FetchedItem>) :
    RecyclerView.Adapter<ItemsResultViewHolder>() {

    private val items = arrayListOf<FetchedItem?>()

    init {
        items.addAll(initItems)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsResultViewHolder {
        val viewLayout =
            if (viewType == 0) R.layout.items_result_loading else R.layout.items_result_item
        val activity = parent.context as ItemsSearchActivity
        val view = LayoutInflater.from(activity)
            .inflate(viewLayout, parent, false)
        return ItemsResultViewHolder(view, activity)
    }

    override fun onBindViewHolder(holder: ItemsResultViewHolder, position: Int) {
        val item = items.getOrNull(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int) =
        if (items.getOrNull(position) == null) 0 else 1

    fun addLoader() {
        items.add(null)
        notifyItemInserted(items.size - 1)
    }

    fun addFetchedItems(fetchedItems: List<FetchedItem>) {
        items.remove(null)
        items.addAll(fetchedItems)
        notifyItemRangeChanged(items.size, fetchedItems.size)
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int) = items.getOrNull(position).hashCode().toLong()
}

@ExperimentalCoroutinesApi
class ItemsResultViewHolder(
    itemView: View,
    activity: ItemsSearchActivity
) :
    RecyclerView.ViewHolder(itemView) {
    private val leftHeaderPart = itemView.findViewById<ImageView>(R.id.leftHeaderPart)
    private val leftHeaderSymbol = itemView.findViewById<ImageView>(R.id.leftHeaderSymbol)
    private val middleHeaderPart = itemView.findViewById<TextView>(R.id.middleHeaderPart)
    private val rightHeaderPart = itemView.findViewById<ImageView>(R.id.rightHeaderPart)
    private val rightHeaderSymbol = itemView.findViewById<ImageView>(R.id.rightHeaderSymbol)

    private val itemImageContainer = itemView.findViewById<FrameLayout>(R.id.itemImageContainer)
    private val itemImage = itemView.findViewById<ImageView>(R.id.itemImage)

    private val itemTextData = itemView.findViewById<TextView>(R.id.itemTextData)

    private val hybridItemGroup = itemView.findViewById<Group>(R.id.hybridItemGroup)
    private val bottomHybridItemTextSeparator =
        itemView.findViewById<ImageView>(R.id.bottomHybridItemTextSeparator)
    private val topHybridItemTextSeparator =
        itemView.findViewById<ImageView>(R.id.topHybridItemTextSeparator)
    private val hybridItemTextHeader = itemView.findViewById<TextView>(R.id.hybridItemTextHeader)
    private val hybridItemTextData = itemView.findViewById<TextView>(R.id.hybridItemTextData)

    private val imageLoader = activity.imageLoader
    private val socketsTemplate = activity.socketsTemplate

    fun bind(item: FetchedItem) {
        itemView.setBackgroundColor(itemView.context.getColor(item.backgroundColorId))

        val hasName = !item.name.isNullOrBlank()

        if (item.frameType != null) {
            setFrame(item.frameType, hasName)
        }

        when {
            item.influenceIcons.size > 1 -> {
                item.influenceIcons[0]?.let { ContextCompat.getDrawable(itemView.context, it) }
                    .also {
                        leftHeaderSymbol.setImageDrawable(it)
                    }
                item.influenceIcons[1]?.let { ContextCompat.getDrawable(itemView.context, it) }
                    .also {
                        rightHeaderSymbol.setImageDrawable(it)
                    }
            }
            item.influenceIcons.size == 1 -> {
                item.influenceIcons[0]?.let { ContextCompat.getDrawable(itemView.context, it) }
                    .also {
                        leftHeaderSymbol.setImageDrawable(it)
                        rightHeaderSymbol.setImageDrawable(it)
                    }
            }
        }

        itemTextData.setText(item.itemTextData, TextView.BufferType.SPANNABLE)

        if (item.hybridItemTextData != null) {
            val separator =
                ContextCompat.getDrawable(itemView.context, getSeparator(item.frameType))
            bottomHybridItemTextSeparator.setImageDrawable(separator)
            topHybridItemTextSeparator.setImageDrawable(separator)
            hybridItemTextHeader.text = item.typeLine
            middleHeaderPart.text = item.hybridTypeLine
            hybridItemTextData.setText(item.hybridItemTextData, TextView.BufferType.SPANNABLE)
            hybridItemGroup.visibility = View.VISIBLE
        } else {
            middleHeaderPart.text = if (hasName) "${item.name}\n${item.typeLine}" else item.typeLine
        }

        GlobalScope.launch(Dispatchers.Default) {
            val request = ImageRequest.Builder(itemView.context)
                .data(item.iconUrl)
                .allowHardware(false)
                .build()
            val dpi = itemView.context.resources.displayMetrics.density
            imageLoader.execute(request).drawable?.let { icon ->
                GlobalScope.launch(Dispatchers.Main) {
                    itemImage.minimumWidth = (icon.intrinsicWidth * dpi).toInt()
                    itemImage.minimumHeight = (icon.intrinsicHeight * dpi).toInt()
                    itemImage.setImageDrawable(icon)
                    if (item.sockets != null) {
                        itemImageContainer.addView(socketsTemplate.prepareTemplate(item.sockets))
                    }
                }
            }
        }
    }

    private fun setFrame(frameType: Int, hasName: Boolean) {
        when (frameType) {
            0 -> {
                leftHeaderPart.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.header_normal_left
                    )
                )
                middleHeaderPart.background = ContextCompat.getDrawable(
                    itemView.context,
                    R.drawable.header_normal_middle_background
                )
                rightHeaderPart.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.header_normal_right
                    )
                )
            }
            1 -> {
                leftHeaderPart.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.header_magic_left
                    )
                )
                middleHeaderPart.background = ContextCompat.getDrawable(
                    itemView.context,
                    R.drawable.header_magic_middle_background
                )
                rightHeaderPart.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.header_magic_right
                    )
                )
            }
            2 -> {
                leftHeaderPart.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemView.context,
                        if (hasName) R.drawable.header_double_rare_left
                        else R.drawable.header_rare_left
                    )
                )
                middleHeaderPart.background = ContextCompat.getDrawable(
                    itemView.context,
                    if (hasName) R.drawable.header_double_rare_middle_background
                    else R.drawable.header_rare_middle_background
                )
                rightHeaderPart.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemView.context,
                        if (hasName) R.drawable.header_double_rare_right
                        else R.drawable.header_rare_right
                    )
                )
            }
            3 -> {
                leftHeaderPart.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemView.context,
                        if (hasName) R.drawable.header_double_unique_left
                        else R.drawable.header_unique_left
                    )
                )
                middleHeaderPart.background = ContextCompat.getDrawable(
                    itemView.context,
                    if (hasName) R.drawable.header_double_unique_middle_background
                    else R.drawable.header_unique_middle_background
                )
                rightHeaderPart.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemView.context,
                        if (hasName) R.drawable.header_double_unique_right
                        else R.drawable.header_unique_right
                    )
                )
            }
            4 -> {
                leftHeaderPart.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.header_gem_left
                    )
                )
                middleHeaderPart.background = ContextCompat.getDrawable(
                    itemView.context,
                    R.drawable.header_gem_middle_background
                )
                rightHeaderPart.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.header_gem_right
                    )
                )
            }
            5 -> {
                leftHeaderPart.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.header_currency_left
                    )
                )
                middleHeaderPart.background = ContextCompat.getDrawable(
                    itemView.context,
                    R.drawable.header_currency_middle_background
                )
                rightHeaderPart.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.header_currency_right
                    )
                )
            }
        }
    }
}