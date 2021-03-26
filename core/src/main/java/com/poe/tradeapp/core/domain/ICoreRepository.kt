package com.poe.tradeapp.core.domain

import com.poe.tradeapp.core.data.models.League
import com.poe.tradeapp.core.data.models.SearchItems
import com.poe.tradeapp.core.data.models.StatGroup
import com.poe.tradeapp.core.data.models.StaticGroup

interface ICoreRepository {
    suspend fun getCurrencyItems(): List<StaticGroup>
    suspend fun getItems(): List<SearchItems>
    suspend fun getStats(): List<StatGroup>
    suspend fun getLeagues(): List<League>
}