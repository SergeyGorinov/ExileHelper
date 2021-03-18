package com.poe.tradeapp.currency.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.poe.tradeapp.currency.data.repository.FeatureRepository
import com.poe.tradeapp.currency.data.services.ApiService
import com.poe.tradeapp.currency.domain.repository.IFeatureRepository
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit

internal val dataModule = module {
    single {
        Retrofit.Builder()
            .baseUrl("https://www.pathofexile.com/")
            .addConverterFactory(Json.asConverterFactory(MediaType.get("application/json")))
            .build()
            .create(ApiService::class.java) as ApiService
    }
    single {
        FeatureRepository(get())
    } bind IFeatureRepository::class
}