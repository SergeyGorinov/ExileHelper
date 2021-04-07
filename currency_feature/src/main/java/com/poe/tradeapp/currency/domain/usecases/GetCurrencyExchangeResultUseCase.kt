package com.poe.tradeapp.currency.domain.usecases

import com.poe.tradeapp.core.domain.ICoreRepository
import com.poe.tradeapp.currency.domain.models.CurrencyResultItem
import com.poe.tradeapp.currency.domain.repository.IFeatureRepository

internal class GetCurrencyExchangeResultUseCase(
    private val repository: IFeatureRepository,
    private val coreRepository: ICoreRepository
) {

    suspend fun execute(
        league: String,
        isFullfilable: Boolean,
        minimum: String?,
        position: Int
    ): List<CurrencyResultItem> {
        return repository.getCurrencyExchangeData(league, isFullfilable, minimum, position)
            .mapNotNull { resultItem ->
                val buyingItem = coreRepository.staticData.flatMap {
                    it.entries
                }.firstOrNull {
                    it.id == resultItem.getCurrencyId
                }
                val payingItem = coreRepository.staticData.flatMap {
                    it.entries
                }.firstOrNull {
                    it.id == resultItem.payCurrencyId
                }
                if (buyingItem != null && payingItem != null) {
                    CurrencyResultItem(
                        resultItem.stock,
                        resultItem.pay,
                        resultItem.get,
                        payingItem.text,
                        "${"https://www.pathofexile.com"}${payingItem.image}",
                        buyingItem.text,
                        "${"https://www.pathofexile.com"}${buyingItem.image}",
                        resultItem.accountName,
                        resultItem.lastCharacterName,
                        resultItem.status
                    )
                } else {
                    null
                }
            }
    }
}