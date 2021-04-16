package com.sgorinov.exilehelper.charts_feature.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.sgorinov.exilehelper.charts_feature.domain.IFeatureRepository
import com.sgorinov.exilehelper.core.presentation.FragmentScopes
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit

internal val dataModule = module {
    scope(named(FragmentScopes.CHARTS_FEATURE)) {
        scoped {
            Retrofit.Builder()
                .baseUrl("https://poe.ninja/")
                .addConverterFactory(Json.asConverterFactory(MediaType.get("application/json")))
                .build()
                .create(PoeNinjaChartsApi::class.java) as PoeNinjaChartsApi
        }
        scoped {
            FeatureRepository(get())
        } bind IFeatureRepository::class
    }
}