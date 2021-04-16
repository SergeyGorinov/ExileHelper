package com.sgorinov.exilehelper

import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

internal class MyFirebaseMessaging : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        val payload = message.data.getOrElse("data") {
            return
        }
        val intent = Intent(currencyMessage).apply {
            putExtra(currencyMessagePayload, payload)
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    companion object {
        const val currencyMessage = "FIREBASE_CURRENCY_MESSAGE"
        const val currencyMessagePayload = "FIREBASE_CURRENCY_MESSAGE_PAYLOAD"
    }
}