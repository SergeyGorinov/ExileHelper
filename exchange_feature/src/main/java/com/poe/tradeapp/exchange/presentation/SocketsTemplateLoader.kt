package com.poe.tradeapp.exchange.presentation

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import com.poe.tradeapp.exchange.R
import com.poe.tradeapp.exchange.data.models.Socket
import com.poe.tradeapp.exchange.databinding.SocketsLayoutBinding

class SocketsTemplateLoader(context: Context) {

    private val redSocket: Drawable? =
        ContextCompat.getDrawable(context, R.drawable.red_socket)
    private val greenSocket: Drawable? =
        ContextCompat.getDrawable(context, R.drawable.green_socket)
    private val blueSocket: Drawable? =
        ContextCompat.getDrawable(context, R.drawable.blue_socket)
    private val whiteSocket: Drawable? =
        ContextCompat.getDrawable(context, R.drawable.white_socket)
    private val horizontalConnector: Drawable? =
        ContextCompat.getDrawable(context, R.drawable.horizontal_connector)
    private val verticalConnector: Drawable? =
        ContextCompat.getDrawable(context, R.drawable.vertical_connector)

    private val viewBinding =
        SocketsLayoutBinding.inflate(LayoutInflater.from(context), null, false)

    fun prepareTemplate(sockets: List<Socket>): View {
        var socketCount = sockets.size
        val socketGroups = if (socketCount % 2 != 0) {
            socketCount / 2 + 1
        } else {
            socketCount / 2
        }
        for (i in 0 until socketGroups) {
            socketCount -= prepareSocketGroup(i, socketCount, sockets)
        }
        return viewBinding.root
    }

    private fun prepareSocketGroup(
        groupId: Int,
        socketCount: Int,
        sockets: List<Socket>
    ): Int {
        when (groupId) {
            0 -> {
                viewBinding.socket1.setImageDrawable(getSocketColor(sockets[0]))
                viewBinding.topSocketGroup.visibility = View.VISIBLE
                viewBinding.socket1.visibility = View.VISIBLE
                if (socketCount >= 2) {
                    viewBinding.socket2.setImageDrawable(getSocketColor(sockets[1]))
                    viewBinding.connector12.visibility = View.VISIBLE
                    if (sockets[0].group == sockets[1].group)
                        viewBinding.connector12.setImageDrawable(horizontalConnector)
                    viewBinding.socket2.visibility = View.VISIBLE
                    return 2
                }
                return 1
            }
            1 -> {
                if (sockets[1].group == sockets[2].group)
                    viewBinding.connector23.setImageDrawable(verticalConnector)
                viewBinding.socket3.setImageDrawable(getSocketColor(sockets[2]))
                viewBinding.connector23.visibility = View.VISIBLE
                viewBinding.middleSocketGroup.visibility = View.VISIBLE
                viewBinding.connector43.visibility = View.VISIBLE
                viewBinding.socket3.visibility = View.VISIBLE
                viewBinding.socket4.visibility = View.VISIBLE
                if (socketCount >= 2) {
                    viewBinding.socket4.setImageDrawable(getSocketColor(sockets[3]))
                    if (sockets[2].group == sockets[3].group)
                        viewBinding.connector43.setImageDrawable(horizontalConnector)
                    return 2
                }
                return 1
            }
            2 -> {
                if (sockets[3].group == sockets[4].group)
                    viewBinding.connector45.setImageDrawable(verticalConnector)
                viewBinding.socket5.setImageDrawable(getSocketColor(sockets[4]))
                viewBinding.connector45.visibility = View.VISIBLE
                viewBinding.bottomSocketGroup.visibility = View.VISIBLE
                viewBinding.socket5.visibility = View.VISIBLE
                viewBinding.connector56.visibility = View.VISIBLE
                viewBinding.socket6.visibility = View.VISIBLE
                if (socketCount >= 2) {
                    viewBinding.socket6.setImageDrawable(getSocketColor(sockets[5]))
                    if (sockets[4].group == sockets[5].group)
                        viewBinding.connector56.setImageDrawable(horizontalConnector)
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