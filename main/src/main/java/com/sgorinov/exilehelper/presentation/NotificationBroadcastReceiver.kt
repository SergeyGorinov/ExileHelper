package com.sgorinov.exilehelper.presentation

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.sgorinov.exilehelper.MyFirebaseMessaging
import com.sgorinov.exilehelper.R
import com.sgorinov.exilehelper.currency.data.models.CurrencyRequest
import com.sgorinov.exilehelper.currency.presentation.fragments.CurrencyExchangeMainFragment
import com.sgorinov.exilehelper.exchange.presentation.fragments.ItemsSearchMainFragment
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

internal class NotificationBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            MyFirebaseMessaging.FIREBASE_NOTIFICATION -> {
                val title =
                    intent.getStringExtra(MyFirebaseMessaging.NOTIFICATION_MESSAGE_TITLE)
                val body = intent.getStringExtra(MyFirebaseMessaging.NOTIFICATION_MESSAGE_BODY)
                val payload = intent.getStringExtra(MyFirebaseMessaging.NOTIFICATION_PAYLOAD)
                val type = intent.getStringExtra(MyFirebaseMessaging.NOTIFICATION_TYPE)
                if (title != null && body != null) {
                    generateNotification(title, body, type to payload, context)
                }
            }
            MyFirebaseMessaging.APP_NOTIFICATION -> {
                val payload = intent.getStringExtra(MyFirebaseMessaging.NOTIFICATION_PAYLOAD)
                val type = intent.getStringExtra(MyFirebaseMessaging.NOTIFICATION_TYPE)
                if (payload != null && type != null) {
                    processNotificationPayload(type, payload, context)
                }
            }
        }
    }

    private fun generateNotification(
        title: String,
        message: String,
        data: Pair<String?, String?>,
        context: Context?
    ) {
        context ?: return
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val intent = Intent(context, NotificationBroadcastReceiver::class.java).apply {
            action = MyFirebaseMessaging.APP_NOTIFICATION
            putExtra(MyFirebaseMessaging.NOTIFICATION_TYPE, data.first)
            putExtra(MyFirebaseMessaging.NOTIFICATION_PAYLOAD, data.second)
        }
        val notificationBuilder = NotificationCompat.Builder(
            context,
            MainActivity.DEFAULT_CHANNEL_ID
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nc = NotificationChannel(
                MainActivity.DEFAULT_CHANNEL_ID,
                "Price notification",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(nc)
        }
        notificationBuilder.setSmallIcon(R.drawable.notification_icon)
        notificationBuilder.setContentTitle(title)
        notificationBuilder.setContentText("Hey, Exile!")
        notificationBuilder.setStyle(NotificationCompat.BigTextStyle().bigText(message))
        notificationBuilder.setAutoCancel(true)
        notificationBuilder.setContentIntent(
            PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        )
        notificationManager.notify("Exile Helper".hashCode(), notificationBuilder.build())
    }

    private fun processNotificationPayload(
        type: String?,
        payload: String?,
        context: Context?
    ) {
        if (type == null || payload == null || context == null) {
            return
        }
        val intent = when (type) {
            CurrencyExchangeMainFragment.NOTIFICATION_REQUESTS_TYPE -> {
                val data = Json.decodeFromString<CurrencyRequest>(payload)
                Intent(CurrencyExchangeMainFragment.NOTIFICATION_ACTION).apply {
                    putExtra(
                        CurrencyExchangeMainFragment.WANT_ITEM_ID_KEY,
                        data.exchange.want.firstOrNull()
                    )
                    putExtra(
                        CurrencyExchangeMainFragment.HAVE_ITEM_ID_KEY,
                        data.exchange.have.firstOrNull()
                    )
                }
            }
            ItemsSearchMainFragment.NOTIFICATION_REQUESTS_TYPE -> {
                val data = Json.decodeFromString(
                    JsonElement.serializer(), payload
                ).jsonObject["query"]
                val parsedType = data?.jsonObject?.get("type")?.jsonPrimitive?.content
                val parsedName = data?.jsonObject?.get("name")?.jsonPrimitive?.content
                Intent(ItemsSearchMainFragment.NOTIFICATION_ACTION).apply {
                    putExtra(ItemsSearchMainFragment.SAVED_ITEM_TYPE, parsedType)
                    putExtra(ItemsSearchMainFragment.SAVED_ITEM_NAME, parsedName)
                }
            }
            else -> null
        }
        if (intent != null) {
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
        }
    }
}