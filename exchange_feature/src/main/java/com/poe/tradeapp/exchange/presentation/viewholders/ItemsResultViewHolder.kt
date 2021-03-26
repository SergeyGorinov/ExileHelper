package com.poe.tradeapp.exchange.presentation.viewholders

import android.os.Build
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.poe.tradeapp.exchange.R
import com.poe.tradeapp.exchange.presentation.SeparatorHelper.getSeparator
import com.poe.tradeapp.exchange.presentation.SocketsTemplateLoader
import com.poe.tradeapp.exchange.presentation.models.FetchedItem
import com.squareup.picasso.Picasso

class ItemsResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

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

    private val socketsTemplate = SocketsTemplateLoader(itemView.context)

    fun bind(item: FetchedItem) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            itemView.setBackgroundColor(itemView.context.getColor(item.backgroundColorId))
        }

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

        Picasso.get().load(item.iconUrl).fit().into(itemImage)
        item.sockets?.let {
            itemImageContainer.addView(socketsTemplate.prepareTemplate(it))
        }
    }

    private fun setFrame(frameType: Int?, hasName: Boolean) {
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