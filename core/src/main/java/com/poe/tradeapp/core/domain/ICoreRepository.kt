package com.poe.tradeapp.core.domain

import com.poe.tradeapp.core.data.models.League
import com.poe.tradeapp.core.data.models.StaticGroup

interface ICoreRepository {
    suspend fun getCurrencyItems(): List<StaticGroup>
    suspend fun getLeagues(): List<League>
}