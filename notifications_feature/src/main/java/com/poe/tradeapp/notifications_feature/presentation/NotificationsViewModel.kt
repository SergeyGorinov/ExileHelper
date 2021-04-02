package com.poe.tradeapp.notifications_feature.presentation

import androidx.lifecycle.ViewModel
import com.poe.tradeapp.core.domain.usecases.GetNotificationRequestsUseCase
import com.poe.tradeapp.notifications_feature.presentation.models.NotificationRequestViewData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

internal class NotificationsViewModel(
    private val getNotificationRequestsUseCase: GetNotificationRequestsUseCase
) : ViewModel() {

    val viewLoadingState = MutableStateFlow(false)

    suspend fun requestNotifications(
        messagingToken: String,
        authToken: String? = null
    ): List<NotificationRequestViewData> {
        return withContext(Dispatchers.IO) {
            viewLoadingState.emit(true)
            val results = getNotificationRequestsUseCase.execute(messagingToken, authToken).map {
                NotificationRequestViewData(
                    it.buyingItem.itemName,
                    it.buyingItem.itemIcon,
                    it.payingItem.itemName,
                    it.payingItem.itemIcon,
                    it.payingAmount
                )
            }
            viewLoadingState.emit(false)
            return@withContext results
        }
    }
}