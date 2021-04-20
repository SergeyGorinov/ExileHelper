package com.sgorinov.exilehelper

import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

internal class MyFirebaseMessaging : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        val payload = message.data["data"]
        val type = message.data["type"]
        val intent = Intent(FIREBASE_NOTIFICATION).apply {
            putExtra(NOTIFICATION_MESSAGE_TITLE, message.notification?.title ?: "")
            putExtra(NOTIFICATION_MESSAGE_BODY, message.notification?.body ?: "")
            if (payload != null && type != null) {
                putExtra(NOTIFICATION_PAYLOAD, payload)
                putExtra(NOTIFICATION_TYPE, type)
            }
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    override fun onNewToken(newToken: String) {
        val intent = Intent(TOKEN_CHANGED).apply {
            putExtra(TOKEN_CHANGED_PAYLOAD, newToken)
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    companion object {
        const val APP_NOTIFICATION = "APP_NOTIFICATION"
        const val FIREBASE_NOTIFICATION = "FIREBASE_NOTIFICATION"
        const val NOTIFICATION_MESSAGE_TITLE = "CURRENCY_MESSAGE_TITLE"
        const val NOTIFICATION_MESSAGE_BODY = "CURRENCY_MESSAGE_BODY"
        const val NOTIFICATION_PAYLOAD = "NOTIFICATION_PAYLOAD"
        const val NOTIFICATION_TYPE = "NOTIFICATION_TYPE"
        const val TOKEN_CHANGED = "FIREBASE_TOKEN_CHANGED"
        const val TOKEN_CHANGED_PAYLOAD = "FIREBASE_TOKEN_CHANGED_PAYLOAD"
    }
}