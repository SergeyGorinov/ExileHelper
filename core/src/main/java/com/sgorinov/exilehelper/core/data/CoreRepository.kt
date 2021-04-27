package com.sgorinov.exilehelper.core.data

import android.util.Log
import com.sgorinov.exilehelper.core.data.models.*
import io.objectbox.kotlin.boxFor
import io.objectbox.kotlin.query
import kotlinx.serialization.json.Json
import retrofit2.Response

internal class CoreRepository(
    private val staticApi: StaticApi,
    private val exileHelperApi: ExileHelperApi
) : BaseCoreRepository() {

    override var leagues: List<String> = listOf()
    override var staticData: List<StaticGroup> = listOf()
    override var itemData: List<ItemGroup> = listOf()
    override var statData: List<StatGroup> = listOf()

    override suspend fun getCurrencyItems() {
        staticData = staticApi.getStaticData().result
    }

    override suspend fun getItems() {
        itemData = staticApi.getItemsData().result
    }

    override suspend fun getStats() {
        statData = staticApi.getStatsData().result
    }

    override suspend fun getLeagues() {
        leagues = staticApi.getLeagueData().result.map { it.id }
    }

    override suspend fun setNotificationRequestRemote(request: RemoteNotificationRequest): Response<Long> {
        return exileHelperApi.sendRequest(request)
    }

    override suspend fun removeRequestRemote(requestId: Long): Response<Void>? {
        val existingRequest = ObjectBox.objectBox.boxFor<NotificationRequest>().query {
            equal(NotificationRequest_.remoteId, requestId)
        }.findFirst()
        return if (existingRequest != null) {
            val response = exileHelperApi.removeRequest(
                RemoteNotificationRequest(
                    existingRequest.remoteId,
                    existingRequest.registrationToken,
                    existingRequest.notificationToken,
                    existingRequest.requestType,
                    existingRequest.requestPayload,
                    existingRequest.buyingItem.itemName,
                    existingRequest.payingItem.itemName,
                    existingRequest.payingAmount,
                    existingRequest.league
                )
            )
            if (response.isSuccessful) {
                ObjectBox.objectBox.boxFor<NotificationRequest>().remove(existingRequest.id)
            }
            response
        } else {
            null
        }
    }

    override suspend fun setNotificationRequestLocal(request: NotificationRequest) {
        ObjectBox.objectBox.boxFor<NotificationRequest>().put(request)
    }

    override suspend fun syncRemoteNotificationRequests(
        messagingToken: String,
        authorizationToken: String?,
        type: String,
        league: String
    ) {
        val result = try {
            val response =
                exileHelperApi.getRequests(authorizationToken, messagingToken, type, league)
            response.body()
        } catch (e: Exception) {
            Log.e("GET REQUESTS ERROR", e.stackTraceToString())
            null
        }?.map {
            NotificationRequest(
                0L,
                it.id,
                it.firebaseAuthenticationToken,
                it.firebaseMessagingToken,
                it.requestType,
                it.requestPayload,
                Json.decodeFromString(ItemData.serializer(), it.buyingItem),
                Json.decodeFromString(ItemData.serializer(), it.payingItem),
                it.payingAmount,
                it.league
            )
        }
        if (result != null) {
            val localRequests = ObjectBox.objectBox.boxFor<NotificationRequest>().all
            localRequests.filterNot { result.contains(it) }.forEach {
                ObjectBox.objectBox.boxFor<NotificationRequest>().remove(it.id)
            }
        } else {
            ObjectBox.objectBox.boxFor<NotificationRequest>().removeAll()
        }
        ObjectBox.objectBox.boxFor<NotificationRequest>().put(result)
    }

    override suspend fun getNotificationRequestsLocal(): List<NotificationRequest> {
        return ObjectBox.objectBox.boxFor<NotificationRequest>().all
    }

    override suspend fun addToken(messagingToken: String, authorizationToken: String): Boolean {
        return try {
            exileHelperApi.addToken(authorizationToken, messagingToken).isSuccessful
        } catch (e: Exception) {
            false
        }
    }
}