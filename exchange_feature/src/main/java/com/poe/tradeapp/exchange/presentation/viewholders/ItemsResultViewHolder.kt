package com.poe.tradeapp.exchange.presentation.viewholders

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.poe.tradeapp.core.presentation.PicassoImageScaleTransform
import com.poe.tradeapp.exchange.R
import com.poe.tradeapp.exchange.databinding.ItemsResultItemBinding
import com.poe.tradeapp.exchange.databinding.ItemsResultItemHeaderBinding
import com.poe.tradeapp.exchange.databinding.ItemsResultItemHybridBinding
import com.poe.tradeapp.exchange.presentation.SeparatorHelper.getSeparator
import com.poe.tradeapp.exchange.presentation.models.FetchedItem
import com.poe.tradeapp.exchange.presentation.models.Socket
import com.squareup.picasso.Picasso

internal class ItemsResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val picassoImageScaleTransform = PicassoImageScaleTransform()

    private val viewBinding = ItemsResultItemBinding.bind(itemView)
    private val headerViewBinding = ItemsResultItemHeaderBinding.bind(viewBinding.root)
    private val hybridViewBinding = ItemsResultItemHybridBinding.bind(viewBinding.root)

    private val redSocket: Drawable? =
        ContextCompat.getDrawable(itemView.context, R.drawable.red_socket)
    private val greenSocket: Drawable? =
        ContextCompat.getDrawable(itemView.context, R.drawable.green_socket)
    private val blueSocket: Drawable? =
        ContextCompat.getDrawable(itemView.context, R.drawable.blue_socket)
    private val whiteSocket: Drawable? =
        ContextCompat.getDrawable(itemView.context, R.drawable.white_socket)
    private val horizontalConnector: Drawable? =
        ContextCompat.getDrawable(itemView.context, R.drawable.horizontal_connector)
    private val verticalConnector: Drawable? =
        ContextCompat.getDrawable(itemView.context, R.drawable.vertical_connector)

    fun bind(item: FetchedItem, isOddItem: Boolean) {
        val color = ContextCompat.getColor(
            itemView.context,
            if (isOddItem) R.color.odd_result_row_color else R.color.even_result_row_color
        )
        itemView.setBackgroundColor(color)

        val hasName = !item.name.isNullOrBlank()

        if (item.frameType != null) {
            setFrame(item.frameType, hasName)
        }

        when {
            item.influenceIcons.size > 1 -> {
                item.influenceIcons[0]?.let { ContextCompat.getDrawable(itemView.context, it) }
                    .also {
                        headerViewBinding.leftHeaderSymbol.setImageDrawable(it)
                    }
                item.influenceIcons[1]?.let { ContextCompat.getDrawable(itemView.context, it) }
                    .also {
                        headerViewBinding.rightHeaderSymbol.setImageDrawable(it)
                    }
            }
            item.influenceIcons.size == 1 -> {
                item.influenceIcons[0]?.let { ContextCompat.getDrawable(itemView.context, it) }
                    .also {
                        headerViewBinding.leftHeaderSymbol.setImageDrawable(it)
                        headerViewBinding.rightHeaderSymbol.setImageDrawable(it)
                    }
            }
        }

        hybridViewBinding.itemTextData.setText(item.itemTextData, TextView.BufferType.SPANNABLE)

        if (!item.hybridItemTextData.isNullOrBlank()) {
            val separator =
                ContextCompat.getDrawable(itemView.context, getSeparator(item.frameType))
            hybridViewBinding.bottomHybridItemTextSeparator.setImageDrawable(separator)
            hybridViewBinding.topHybridItemTextSeparator.setImageDrawable(separator)
            hybridViewBinding.hybridItemTextHeader.text = item.typeLine
            headerViewBinding.middleHeaderPart.text = item.hybridTypeLine
            hybridViewBinding.hybridItemTextData.setText(
                item.hybridItemTextData,
                TextView.BufferType.SPANNABLE
            )
            hybridViewBinding.hybridItemGroup.visibility = View.VISIBLE
        } else {
            headerViewBinding.middleHeaderPart.text =
                if (hasName) "${item.name}\n${item.typeLine}" else item.typeLine
        }

        Picasso
            .get()
            .load(item.iconUrl)
            .transform(picassoImageScaleTransform)
            .into(viewBinding.itemImage)

        prepareTemplate(item.sockets)
    }

    private fun setFrame(frameType: Int?, hasName: Boolean) {
        when (frameType) {
            0 -> {
                headerViewBinding.leftHeaderPart.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.header_normal_left
                    )
                )
                headerViewBinding.middleHeaderPart.background = ContextCompat.getDrawable(
                    itemView.context,
                    R.drawable.header_normal_middle_background
                )
                headerViewBinding.rightHeaderPart.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.header_normal_right
                    )
                )
            }
            1 -> {
                headerViewBinding.leftHeaderPart.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.header_magic_left
                    )
                )
                headerViewBinding.middleHeaderPart.background = ContextCompat.getDrawable(
                    itemView.context,
                    R.drawable.header_magic_middle_background
                )
                headerViewBinding.rightHeaderPart.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.header_magic_right
                    )
                )
            }
            2 -> {
                headerViewBinding.leftHeaderPart.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemView.context,
                        if (hasName) R.drawable.header_double_rare_left
                        else R.drawable.header_rare_left
                    )
                )
                headerViewBinding.middleHeaderPart.background = ContextCompat.getDrawable(
                    itemView.context,
                    if (hasName) R.drawable.header_double_rare_middle_background
                    else R.drawable.header_rare_middle_background
                )
                headerViewBinding.rightHeaderPart.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemView.context,
                        if (hasName) R.drawable.header_double_rare_right
                        else R.drawable.header_rare_right
                    )
                )
            }
            3 -> {
                headerViewBinding.leftHeaderPart.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemView.context,
                        if (hasName) R.drawable.header_double_unique_left
                        else R.drawable.header_unique_left
                    )
                )
                headerViewBinding.middleHeaderPart.background = ContextCompat.getDrawable(
                    itemView.context,
                    if (hasName) R.drawable.header_double_unique_middle_background
                    else R.drawable.header_unique_middle_background
                )
                headerViewBinding.rightHeaderPart.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemView.context,
                        if (hasName) R.drawable.header_double_unique_right
                        else R.drawable.header_unique_right
                    )
                )
            }
            4 -> {
                headerViewBinding.leftHeaderPart.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.header_gem_left
                    )
                )
                headerViewBinding.middleHeaderPart.background = ContextCompat.getDrawable(
                    itemView.context,
                    R.drawable.header_gem_middle_background
                )
                headerViewBinding.rightHeaderPart.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.header_gem_right
                    )
                )
            }
            5 -> {
                headerViewBinding.leftHeaderPart.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.header_currency_left
                    )
                )
                headerViewBinding.middleHeaderPart.background = ContextCompat.getDrawable(
                    itemView.context,
                    R.drawable.header_currency_middle_background
                )
                headerViewBinding.rightHeaderPart.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.header_currency_right
                    )
                )
            }
        }
    }

    private fun prepareTemplate(sockets: List<Socket>?) {
        sockets ?: return
        var socketCount = sockets.size
        val socketGroups = if (socketCount % 2 != 0) {
            socketCount / 2 + 1
        } else {
            socketCount / 2
        }
        for (i in 0 until socketGroups) {
            socketCount -= prepareSocketGroup(i, socketCount, sockets)
        }
    }

    private fun prepareSocketGroup(
        groupId: Int,
        socketCount: Int,
        sockets: List<Socket>
    ): Int {
        when (groupId) {
            0 -> {
                viewBinding.socketsTemplateLayout.socket1.setImageDrawable(getSocketColor(sockets[0]))
                viewBinding.socketsTemplateLayout.topSocketGroup.visibility = View.VISIBLE
                viewBinding.socketsTemplateLayout.socket1.visibility = View.VISIBLE
                if (socketCount >= 2) {
                    viewBinding.socketsTemplateLayout.socket2.setImageDrawable(
                        getSocketColor(
                            sockets[1]
                        )
                    )
                    viewBinding.socketsTemplateLayout.connector12.visibility = View.VISIBLE
                    if (sockets[0].group == sockets[1].group)
                        viewBinding.socketsTemplateLayout.connector12.setImageDrawable(
                            horizontalConnector
                        )
                    viewBinding.socketsTemplateLayout.socket2.visibility = View.VISIBLE
                    return 2
                }
                return 1
            }
            1 -> {
                if (sockets[1].group == sockets[2].group)
                    viewBinding.socketsTemplateLayout.connector23.setImageDrawable(verticalConnector)
                viewBinding.socketsTemplateLayout.socket3.setImageDrawable(getSocketColor(sockets[2]))
                viewBinding.socketsTemplateLayout.connector23.visibility = View.VISIBLE
                viewBinding.socketsTemplateLayout.middleSocketGroup.visibility = View.VISIBLE
                viewBinding.socketsTemplateLayout.connector43.visibility = View.VISIBLE
                viewBinding.socketsTemplateLayout.socket3.visibility = View.VISIBLE
                viewBinding.socketsTemplateLayout.socket4.visibility = View.VISIBLE
                if (socketCount >= 2) {
                    viewBinding.socketsTemplateLayout.socket4.setImageDrawable(
                        getSocketColor(
                            sockets[3]
                        )
                    )
                    if (sockets[2].group == sockets[3].group)
                        viewBinding.socketsTemplateLayout.connector43.setImageDrawable(
                            horizontalConnector
                        )
                    return 2
                }
                return 1
            }
            2 -> {
                if (sockets[3].group == sockets[4].group)
                    viewBinding.socketsTemplateLayout.connector45.setImageDrawable(verticalConnector)
                viewBinding.socketsTemplateLayout.socket5.setImageDrawable(getSocketColor(sockets[4]))
                viewBinding.socketsTemplateLayout.connector45.visibility = View.VISIBLE
                viewBinding.socketsTemplateLayout.bottomSocketGroup.visibility = View.VISIBLE
                viewBinding.socketsTemplateLayout.socket5.visibility = View.VISIBLE
                viewBinding.socketsTemplateLayout.connector56.visibility = View.VISIBLE
                viewBinding.socketsTemplateLayout.socket6.visibility = View.VISIBLE
                if (socketCount >= 2) {
                    viewBinding.socketsTemplateLayout.socket6.setImageDrawable(
                        getSocketColor(
                            sockets[5]
                        )
                    )
                    if (sockets[4].group == sockets[5].group)
                        viewBinding.socketsTemplateLayout.connector56.setImageDrawable(
                            horizontalConnector
                        )
                    return 2
                }
                return 1
            }
            else ->
                return 0
        }
    }

    private fun getSocketColor(socket: Socket): Drawable? {
        when (socket.sColour) {
            "R" -> {
                return redSocket
            }
            "G" -> {
                return greenSocket
            }
            "B" -> {
                return blueSocket
            }
            "W" -> {
                return whiteSocket
            }
            else -> {
                return null
            }
        }
    }
}