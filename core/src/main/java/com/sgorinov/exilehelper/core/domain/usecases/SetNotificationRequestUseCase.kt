package com.sgorinov.exilehelper.core.domain.usecases

import com.sgorinov.exilehelper.core.data.models.ItemData
import com.sgorinov.exilehelper.core.data.models.RemoteNotificationRequest
import com.sgorinov.exilehelper.core.domain.ICoreRepository
import com.sgorinov.exilehelper.core.domain.models.NotificationItemData
import com.sgorinov.exilehelper.core.domain.models.NotificationRequest
import kotlinx.serialization.json.Json

class SetNotificationRequestUseCase(private val repository: ICoreRepository) {

    suspend fun execute(
        request: NotificationRequest,
        payload: String,
        type: Int,
        messagingToken: String?,
        league: String,
        authToken: String? = null
    ): Boolean {
        messagingToken ?: return false
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
                request.payingAmount,
                league
            )
        )
        if (response.isSuccessful) {
            repository.setNotificationRequestLocal(
                com.sgorinov.exilehelper.core.data.models.NotificationRequest(
                    registrationToken = authToken,
                    notificationToken = messagingToken,
                    requestType = type,
                    requestPayload = payload,
                    buyingItem = ItemData(request.buyingItem.itemName, request.buyingItem.itemIcon),
                    payingItem = ItemData(request.payingItem.itemName, request.payingItem.itemIcon),
                    payingAmount = request.payingAmount,
                    league = league
                )
            )
        }
        return response.isSuccessful
    }
}