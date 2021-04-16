package com.sgorinov.exilehelper.core.data.models

import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.converter.PropertyConverter
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Entity
data class NotificationRequest(
    @Id
    var id: Long = 0L,
    var registrationToken: String? = null,
    var notificationToken: String,
    var requestType: Int,
    var requestPayload: String,
    @Convert(converter = ItemDataConverter::class, dbType = String::class)
    var buyingItem: ItemData,
    @Convert(converter = ItemDataConverter::class, dbType = String::class)
    var payingItem: ItemData,
    var payingAmount: Int
) {
    internal class ItemDataConverter : PropertyConverter<ItemData, String> {
        override fun convertToEntityProperty(databaseValue: String): ItemData {
            return Json.decodeFromString(ItemData.serializer(), databaseValue)
        }

        override fun convertToDatabaseValue(entityProperty: ItemData): String {
            return Json.encodeToString(ItemData.serializer(), entityProperty)
        }

    }
}

@Serializable
data class ItemData(
    val itemName: String,
    val itemIcon: String
)