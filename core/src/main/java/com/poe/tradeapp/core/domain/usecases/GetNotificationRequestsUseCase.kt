package com.poe.tradeapp.core.domain.usecases

import com.poe.tradeapp.core.domain.ICoreRepository
import com.poe.tradeapp.core.domain.models.NotificationItemData
import com.poe.tradeapp.core.domain.models.NotificationRequest

class GetNotificationRequestsUseCase(private val repository: ICoreRepository) {

    suspend fun execute(
        messagingToken: String?,
        authorizationToken: String? = null,
        type: String
    ): List<NotificationRequest> {
        messagingToken ?: return emptyList()
        repository.syncRemoteNotificationRequests(messagingToken, authorizationToken, type)
        return repository.getNotificationRequestsLocal().map { request ->
            NotificationRequest(
                NotificationItemData(request.buyingItem.itemName, request.buyingItem.itemIcon),
                NotificationItemData(request.payingItem.itemName, request.payingItem.itemIcon),
                request.payingAmount
            )
        }
    }
}