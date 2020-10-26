package com.poetradeapp.models.database

import androidx.room.*
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.jetbrains.annotations.NotNull

data class Option(
    val id: Int,
    val text: String
)

@Entity
data class StatGroupModel(
    @PrimaryKey(autoGenerate = true)
    var statGroupId: Int? = null,
    @NotNull
    val label: String
)

@Entity
@TypeConverters(OptionTypeConverters::class)
data class StatModel(
    @PrimaryKey(autoGenerate = true)
    var statId: Int? = null,
    var groupId: Long? = null,
    @NotNull
    val id: String,
    @NotNull
    val text: String,
    @NotNull
    val type: String,
    val options: List<Option>? = null
)

data class StatGroupWithItems(
    @Embedded
    val group: StatGroupModel,
    @Relation(
        parentColumn = "statGroupId",
        entityColumn = "groupId",
        entity = StatModel::class
    )
    val items: List<StatModel>
)

class OptionTypeConverters {

    private val converter = jacksonObjectMapper()

    @TypeConverter
    fun stringToOptionList(data: String?): List<Option>? {
        if (data == null) {
            return emptyList()
        }

        val collectionType =
            converter.typeFactory.constructCollectionType(List::class.java, Option::class.java)

        return converter.readValue(data, collectionType)
    }

    @TypeConverter
    fun optionListToString(data: List<Option>?): String? {
        return if (data == null) null else converter.writeValueAsString(data)
    }

}