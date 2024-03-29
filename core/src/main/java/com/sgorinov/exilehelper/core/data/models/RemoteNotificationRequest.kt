package com.sgorinov.exilehelper.core.data.models

import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

@Serializable
data class RemoteNotificationRequest(
    var id: Long?,
    @Required
    var firebaseAuthenticationToken: String? = null,
    var firebaseMessagingToken: String,
    var requestType: Int,
    var requestPayload: String,
    var buyingItem: String,
    var payingItem: String,
    var payingAmount: Int,
    var league: String
)
