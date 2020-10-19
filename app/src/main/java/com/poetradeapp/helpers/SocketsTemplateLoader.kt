package com.poetradeapp.helpers

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.poetradeapp.R
import com.poetradeapp.models.Socket
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SocketsTemplateLoader @Inject constructor(private val context: Context) {

    private val redSocket: Drawable? = ContextCompat.getDrawable(context, R.drawable.red_socket)
    private val greenSocket: Drawable? =
        ContextCompat.getDrawable(context, R.drawable.green_socket)
    private val blueSocket: Drawable? = ContextCompat.getDrawable(context, R.drawable.blue_socket)
    private val whiteSocket: Drawable? =
        ContextCompat.getDrawable(context, R.drawable.white_socket)
    private val horizontalConnector: Drawable? =
        ContextCompat.getDrawable(context, R.drawable.horizontal_connector)
    private val verticalConnector: Drawable? =
        ContextCompat.getDrawable(context, R.drawable.vertical_connector)

    private lateinit var topSocketGroup: LinearLayout
    private lateinit var middleSocketGroup: LinearLayout
    private lateinit var bottomSocketGroup: LinearLayout

    private lateinit var socket1: ImageView
    private lateinit var socket2: ImageView
    private lateinit var socket3: ImageView
    private lateinit var socket4: ImageView
    private lateinit var socket5: ImageView
    private lateinit var socket6: ImageView

    private lateinit var connector12: ImageView
    private lateinit var connector23: ImageView
    private lateinit var connector43: ImageView
    private lateinit var connector45: ImageView
    private lateinit var connector56: ImageView

    fun prepareTemplate(sockets: List<Socket>): View {

        val view = LayoutInflater.from(context).inflate(R.layout.sockets_template, null)
        loadNewView(view)

        topSocketGroup.visibility = View.GONE
        middleSocketGroup.visibility = View.GONE
        bottomSocketGroup.visibility = View.GONE

        socket1.visibility = View.GONE
        socket2.visibility = View.GONE
        socket3.visibility = View.GONE
        socket4.visibility = View.GONE
        socket5.visibility = View.GONE
        socket6.visibility = View.GONE

        connector12.visibility = View.GONE
        connector23.visibility = View.GONE
        connector43.visibility = View.GONE
        connector45.visibility = View.GONE
        connector56.visibility = View.GONE

        var socketCount = sockets.size
        val socketGroups = if (socketCount % 2 != 0) {
            socketCount / 2 + 1
        } else {
            socketCount / 2
        }
        for (i in 0 until socketGroups) {
            socketCount -= prepareSocketGroup(i, socketCount, sockets)
        }
        return view
    }

    private fun loadNewView(view: View) {
        topSocketGroup = view.findViewById(R.id.topSocketGroup)
        middleSocketGroup = view.findViewById(R.id.middleSocketGroup)
        bottomSocketGroup = view.findViewById(R.id.bottomSocketGroup)

        socket1 = view.findViewById(R.id.socket1)
        socket2 = view.findViewById(R.id.socket2)
        socket3 = view.findViewById(R.id.socket3)
        socket4 = view.findViewById(R.id.socket4)
        socket5 = view.findViewById(R.id.socket5)
        socket6 = view.findViewById(R.id.socket6)

        connector12 = view.findViewById(R.id.connector1_2)
        connector23 = view.findViewById(R.id.connector2_3)
        connector43 = view.findViewById(R.id.connector4_3)
        connector45 = view.findViewById(R.id.connector4_5)
        connector56 = view.findViewById(R.id.connector5_6)
    }

    private fun prepareSocketGroup(
        groupId: Int,
        socketCount: Int,
        sockets: List<Socket>
    ): Int {

        when (groupId) {
            0 -> {
                socket1.setImageDrawable(getSocketColor(sockets[0]))
                topSocketGroup.visibility = View.VISIBLE
                socket1.visibility = View.VISIBLE
                if (socketCount >= 2) {
                    socket2.setImageDrawable(getSocketColor(sockets[1]))
                    connector12.visibility = View.VISIBLE
                    if (sockets[0].group == sockets[1].group)
                        connector12.setImageDrawable(horizontalConnector)
                    socket2.visibility = View.VISIBLE
                    return 2
                }
                return 1
            }
            1 -> {
                if (sockets[1].group == sockets[2].group)
                    connector23.setImageDrawable(verticalConnector)
                socket3.setImageDrawable(getSocketColor(sockets[2]))
                connector23.visibility = View.VISIBLE
                middleSocketGroup.visibility = View.VISIBLE
                connector43.visibility = View.VISIBLE
                socket3.visibility = View.VISIBLE
                socket4.visibility = View.VISIBLE
                if (socketCount >= 2) {
                    socket4.setImageDrawable(getSocketColor(sockets[3]))
                    if (sockets[2].group == sockets[3].group)
                        connector43.setImageDrawable(horizontalConnector)
                    return 2
                }
                return 1
            }
            2 -> {
                if (sockets[3].group == sockets[4].group)
                    connector45.setImageDrawable(verticalConnector)
                socket5.setImageDrawable(getSocketColor(sockets[4]))
                connector45.visibility = View.VISIBLE
                bottomSocketGroup.visibility = View.VISIBLE
                socket5.visibility = View.VISIBLE
                connector56.visibility = View.VISIBLE
                socket6.visibility = View.VISIBLE
                if (socketCount >= 2) {
                    socket6.setImageDrawable(getSocketColor(sockets[5]))
                    if (sockets[4].group == sockets[5].group)
                        connector56.setImageDrawable(horizontalConnector)
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