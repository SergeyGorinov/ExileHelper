package com.sgorinov.exilehelper.exchange_feature.presentation.viewholders

import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.sgorinov.exilehelper.core.presentation.PicassoImageScaleTransform
import com.sgorinov.exilehelper.exchange_feature.R
import com.sgorinov.exilehelper.exchange_feature.databinding.ItemsResultItemBinding
import com.sgorinov.exilehelper.exchange_feature.databinding.ItemsResultItemHeaderBinding
import com.sgorinov.exilehelper.exchange_feature.databinding.ItemsResultItemHybridBinding
import com.sgorinov.exilehelper.exchange_feature.presentation.SeparatorHelper.getSeparator
import com.sgorinov.exilehelper.exchange_feature.presentation.models.FetchedItem
import com.sgorinov.exilehelper.exchange_feature.presentation.models.Socket
import com.squareup.picasso.Picasso

internal class ItemsResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val picassoImageScaleTransform = PicassoImageScaleTransform()

    private val viewBinding = ItemsResultItemBinding.bind(itemView)
    private val headerViewBinding = ItemsResultItemHeaderBinding.bind(viewBinding.root)
    private val hybridViewBinding = ItemsResultItemHybridBinding.bind(viewBinding.root)

    fun bind(item: FetchedItem) {
        val hasName = !item.name.isNullOrBlank()

        setFrame(item.frameType, hasName)

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

        if (item.sockets != null) {
            prepareSockets(item.sockets)
            viewBinding.sockets.root.visibility = View.VISIBLE
        } else {
            viewBinding.sockets.root.visibility = View.GONE
        }
    }

    private fun setFrame(frameType: Int?, hasName: Boolean) {
        val leftHeaderDrawable = when (frameType) {
            0 -> R.drawable.header_normal_left
            1 -> R.drawable.header_magic_left
            2 -> if (hasName) R.drawable.header_double_rare_left else R.drawable.header_rare_left
            3 -> if (hasName) R.drawable.header_double_unique_left else R.drawable.header_unique_left
            4 -> R.drawable.header_gem_left
            5 -> R.drawable.header_currency_left
            else -> R.drawable.header_currency_left
        }
        val middleHeaderDrawable = when (frameType) {
            0 -> R.drawable.header_normal_middle_background
            1 -> R.drawable.header_magic_middle_background
            2 -> if (hasName) {
                R.drawable.header_double_rare_middle_background
            } else {
                R.drawable.header_rare_middle_background
            }
            3 -> if (hasName) {
                R.drawable.header_double_unique_middle_background
            } else {
                R.drawable.header_unique_middle_background
            }
            4 -> R.drawable.header_gem_middle_background
            5 -> R.drawable.header_currency_middle_background
            else -> R.drawable.header_currency_middle_background
        }
        val rightHeaderDrawable = when (frameType) {
            0 -> R.drawable.header_normal_right
            1 -> R.drawable.header_magic_right
            2 -> if (hasName) R.drawable.header_double_rare_right else R.drawable.header_rare_right
            3 -> if (hasName) R.drawable.header_double_unique_right else R.drawable.header_unique_right
            4 -> R.drawable.header_gem_right
            5 -> R.drawable.header_currency_right
            else -> R.drawable.header_currency_right
        }

        headerViewBinding.leftHeaderPart.setBackgroundResource(leftHeaderDrawable)
        headerViewBinding.middleHeaderPart.setBackgroundResource(middleHeaderDrawable)
        headerViewBinding.rightHeaderPart.setBackgroundResource(rightHeaderDrawable)
    }

    private fun prepareSockets(sockets: List<Socket>?) {
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
                viewBinding.sockets.socket1.setImageResource(getSocketColor(sockets[0]))
                viewBinding.sockets.topSocketGroup.visibility = View.VISIBLE
                viewBinding.sockets.socket1.visibility = View.VISIBLE
                if (socketCount >= 2) {
                    viewBinding.sockets.socket2.setImageResource(getSocketColor(sockets[1]))
                    viewBinding.sockets.connector12.visibility = View.VISIBLE
                    if (sockets[0].group == sockets[1].group) {
                        viewBinding.sockets.connector12.setImageResource(
                            R.drawable.horizontal_connector
                        )
                    }
                    viewBinding.sockets.socket2.visibility = View.VISIBLE
                    return 2
                }
                return 1
            }
            1 -> {
                if (sockets[1].group == sockets[2].group) {
                    viewBinding.sockets.connector23.setImageResource(R.drawable.vertical_connector)
                }
                viewBinding.sockets.socket3.setImageResource(getSocketColor(sockets[2]))
                viewBinding.sockets.connector23.visibility = View.VISIBLE
                viewBinding.sockets.middleSocketGroup.visibility = View.VISIBLE
                viewBinding.sockets.connector43.visibility = View.VISIBLE
                viewBinding.sockets.socket3.visibility = View.VISIBLE
                viewBinding.sockets.socket4.visibility = View.VISIBLE
                if (socketCount >= 2) {
                    viewBinding.sockets.socket4.setImageResource(getSocketColor(sockets[3]))
                    if (sockets[2].group == sockets[3].group) {
                        viewBinding.sockets.connector43.setImageResource(
                            R.drawable.horizontal_connector
                        )
                    }
                    return 2
                }
                return 1
            }
            2 -> {
                if (sockets[3].group == sockets[4].group) {
                    viewBinding.sockets.connector45.setImageResource(R.drawable.vertical_connector)
                }
                viewBinding.sockets.socket5.setImageResource(getSocketColor(sockets[4]))
                viewBinding.sockets.connector45.visibility = View.VISIBLE
                viewBinding.sockets.bottomSocketGroup.visibility = View.VISIBLE
                viewBinding.sockets.socket5.visibility = View.VISIBLE
                viewBinding.sockets.connector56.visibility = View.VISIBLE
                viewBinding.sockets.socket6.visibility = View.VISIBLE
                if (socketCount >= 2) {
                    viewBinding.sockets.socket6.setImageResource(getSocketColor(sockets[5]))
                    if (sockets[4].group == sockets[5].group) {
                        viewBinding.sockets.connector56.setImageResource(
                            R.drawable.horizontal_connector
                        )
                    }
                    return 2
                }
                return 1
            }
            else ->
                return 0
        }
    }

    private fun getSocketColor(socket: Socket): Int {
        when (socket.sColour) {
            "R" -> {
                return R.drawable.red_socket
            }
            "G" -> {
                return R.drawable.green_socket
            }
            "B" -> {
                return R.drawable.blue_socket
            }
            "W" -> {
                return R.drawable.white_socket
            }
            else -> {
                return R.drawable.white_socket
            }
        }
    }
}