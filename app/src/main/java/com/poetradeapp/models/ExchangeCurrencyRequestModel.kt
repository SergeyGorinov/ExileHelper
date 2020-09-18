package com.poetradeapp.models

import com.example.poetradeapp.models.Status
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude
data class ExchangeCurrencyRequestModel(
    val exchange: Exchange = Exchange()
)

@JsonInclude
data class Exchange(
    val status: Status = Status("online"),
    val have: List<String> = listOf(),
    val want: List<String> = listOf(),
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val minimum: String? = null,
    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = FullfilableFilter::class)
    val fulfillable: Any? = 0,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val account: String? = null
)

class FullfilableFilter {
    override fun equals(other: Any?): Boolean {
        return other != null
    }
}