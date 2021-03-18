package com.poe.tradeapp.core.domain

import com.poe.tradeapp.core.data.models.StaticGroup

interface ICoreRepository {
    suspend fun getCurrencyItems(): List<StaticGroup>
}