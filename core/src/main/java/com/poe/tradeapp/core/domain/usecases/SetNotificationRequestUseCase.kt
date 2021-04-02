package com.poe.tradeapp.core.domain.usecases

import com.poe.tradeapp.core.data.models.ItemData
import com.poe.tradeapp.core.data.models.RemoteNotificationRequest
import com.poe.tradeapp.core.domain.ICoreRepository
import com.poe.tradeapp.core.domain.models.NotificationItemData
import com.poe.tradeapp.core.domain.models.NotificationRequest
import kotlinx.serialization.json.Json

class SetNotificationRequestUseCase(private val repository: ICoreRepository) {

    suspend fun execute(
        request: NotificationRequest,
        payload: String,
        type: Int,
        messagingToken: String,
        authToken: String? = null
    ): Boolean {
        val buyingItem = Json.encodeToString(NotificationItemData.serializer(), request.buyingItem)
        val payingItem = Json.encodeToString(NotificationItemData.serializer(), request.payingItem)
        val response = repository.setNotificationRequestRemote(
            RemoteNotificationRequest(
                authToken,
                messagingToken,
                type,
                payload,
                buyingItem,
                payingItem,
                request.payingAmount
            )
        )
        if (response.isSuccessful) {
            repository.setNotificationRequestLocal(
                com.poe.tradeapp.core.data.models.NotificationRequest(
                    registrationToken = authToken,
                    notificationToken = messagingToken,
                    requestType = type,
                    requestPayload = payload,
                    buyingItem = ItemData(request.buyingItem.itemName, request.buyingItem.itemIcon),
                    payingItem = ItemData(request.payingItem.itemName, request.payingItem.itemIcon),
                    payingAmount = request.payingAmount
                )
            )
        }
        return response.isSuccessful
    }
}