package com.poe.tradeapp.exchange.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.poe.tradeapp.core.presentation.FragmentScopes
import com.poe.tradeapp.exchange.domain.IFeatureRepository
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit

internal val dataModule = module {
    scope(named(FragmentScopes.EXCHANGE_FEATURE)) {
        scoped {
            Retrofit.Builder()
                .baseUrl("https://www.pathofexile.com/")
                .addConverterFactory(Json.asConverterFactory(MediaType.get("application/json")))
                .build()
                .create(ApiService::class.java)
        } bind ApiService::class
        scoped {
            FeatureRepository(get())
        } bind IFeatureRepository::class
    }
}