package com.poe.tradeapp.charts_feature.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.poe.tradeapp.charts_feature.domain.IFeatureRepository
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit

internal val dataModule = module {
    single {
        Retrofit.Builder()
            .baseUrl("https://poe.ninja/")
            .addConverterFactory(Json.asConverterFactory(MediaType.get("application/json")))
            .build()
            .create(PoeNinjaChartsApi::class.java) as PoeNinjaChartsApi
    }
    single {
        FeatureRepository(get())
    } bind IFeatureRepository::class
}