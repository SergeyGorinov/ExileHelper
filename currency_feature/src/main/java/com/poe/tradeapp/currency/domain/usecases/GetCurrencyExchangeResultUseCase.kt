package com.poe.tradeapp.currency.domain.usecases

import com.poe.tradeapp.currency.domain.models.CurrencyResultItem
import com.poe.tradeapp.currency.domain.repository.IFeatureRepository
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

internal class GetCurrencyExchangeResultUseCase(private val repository: IFeatureRepository) {

    suspend fun execute(
        wantSelectedCurrencies: List<String>,
        haveSelectedCurrencies: List<String>
    ): List<CurrencyResultItem> {
        val rawData =
            repository.getCurrencyExchangeData(wantSelectedCurrencies, haveSelectedCurrencies)
        val data = rawData.getValue("result").jsonArray

        return data.mapNotNull {
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
        }
    }
}