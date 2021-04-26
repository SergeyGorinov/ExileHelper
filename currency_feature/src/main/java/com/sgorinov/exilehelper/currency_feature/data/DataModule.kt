package com.sgorinov.exilehelper.currency_feature.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.sgorinov.exilehelper.core.presentation.FragmentScopes
import com.sgorinov.exilehelper.currency_feature.domain.repository.IFeatureRepository
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit

internal val dataModule = module {
    scope(named(FragmentScopes.CURRENCY_FEATURE)) {
        scoped {
            Retrofit.Builder()
                .baseUrl("https://www.pathofexile.com/")
                .addConverterFactory(Json.asConverterFactory(MediaType.get("application/json")))
                .build()
                .create(ApiService::class.java) as ApiService
        }
        scoped {
            FeatureRepository(get())
        } bind IFeatureRepository::class
    }
}