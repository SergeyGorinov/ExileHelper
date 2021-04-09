package com.poe.tradeapp.core.presentation

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object FirebaseUtils {
    suspend fun getMessagingToken() = suspendCoroutine<String?> { coroutine ->
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            coroutine.resume(it.result)
        }
    }

    suspend fun getAuthToken() = suspendCoroutine<String?> { coroutine ->
        Firebase.auth.currentUser?.getIdToken(false)
            ?.addOnCompleteListener {
                coroutine.resume(it.result?.token)
            } ?: coroutine.resume(null)
    }
}