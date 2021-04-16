package com.sgorinov.exilehelper.currency.data

import com.sgorinov.exilehelper.currency.data.models.*
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

internal class FeatureRepository(private val apiService: ApiService) : BaseFeatureRepository() {

    override val wantCurrencies: MutableList<String> = mutableListOf()
    override val haveCurrencies: MutableList<String> = mutableListOf()

    private var currencyResultListData: CurrencyListResponse? = null
    private val currencyResultData: MutableList<CurrencyResultItem> = mutableListOf()

    override var totalResultCount: Int = 0

    override suspend fun getCurrencyExchangeData(
        league: String,
        isFullfilable: Boolean,
        minimum: String?,
        position: Int
    ): List<CurrencyResultItem> {
        if (position == 0) {
            currencyResultData.clear()
            currencyResultListData = getCurrencyExchangeList(
                league, CurrencyRequest(
                    Exchange(
                        status = Status("online"),
                        want = wantCurrencies,
                        have = haveCurrencies,
                        fulfillable = if (isFullfilable) 0 else null,
                        minimum = minimum
                    )
                )
            )
        }

        val data = currencyResultListData?.let {
            apiService.getCurrencyExchangeResponse(
                if (it.result.size - position >= 20) {
                    it.result.subList(position, position + 20).joinToString(separator = ",")
                } else {
                    it.result.subList(position, position + (it.result.size - position))
                        .joinToString(separator = ",")
                },
                it.id,
                "exchange"
            )
        } ?: return emptyList()

        totalResultCount = currencyResultListData?.result?.size ?: 0

        currencyResultData.addAll(parseCurrencyExchangeData(data))

        return currencyResultData
    }

    private suspend fun getCurrencyExchangeList(
        league: String,
        body: CurrencyRequest
    ): CurrencyListResponse? {
        return apiService.getCurrencyExchangeList(league, body)
    }

    private fun parseCurrencyExchangeData(data: JsonObject): List<CurrencyResultItem> {
        return data["result"]?.jsonArray?.mapNotNull {
            try {
                val value = it as JsonObject
                val listing = value.getValue("listing").jsonObject
                val account = listing.getValue("account").jsonObject
                val price = listing.getValue("price").jsonObject
                val getItemPrice = price.getValue("item").jsonObject
                val payItemPrice = price.getValue("exchange").jsonObject
                val accountName = account.getValue("name").jsonPrimitive.content
                val accountLastCharacterName =
                    account.getValue("lastCharacterName").jsonPrimitive.content
                val accountStatus =
                    account["online"]?.jsonObject?.get("status")?.jsonPrimitive?.content
                val stock = getItemPrice.getValue("stock").jsonPrimitive.content.toInt()
                val getItemAmount = getItemPrice.getValue("amount").jsonPrimitive.content.toInt()
                val payItemAmount = payItemPrice.getValue("amount").jsonPrimitive.content.toInt()
                val getItemId = getItemPrice.getValue("currency").jsonPrimitive.content
                val payItemId = payItemPrice.getValue("currency").jsonPrimitive.content
                CurrencyResultItem(
                    stock,
                    payItemAmount,
                    getItemAmount,
                    payItemId,
                    getItemId,
                    accountName,
                    accountLastCharacterName,
                    accountStatus ?: "Online"
                )
            } catch (e: Exception) {
                null
            }
        } ?: listOf()
    }
}