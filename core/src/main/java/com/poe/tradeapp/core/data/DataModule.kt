package com.poe.tradeapp.core.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.poe.tradeapp.core.domain.ICoreRepository
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
            .create(StaticApi::class.java) as StaticApi
    }
    single {
        CoreRepository(get())
    } bind ICoreRepository::class
}