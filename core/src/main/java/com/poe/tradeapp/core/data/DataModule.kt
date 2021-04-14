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
            .create(StaticApi::class.java)
    } bind StaticApi::class
    single {
        Retrofit.Builder()
            .baseUrl("http://18.156.126.44:8080/")//18.156.126.44//10.0.2.2
            .addConverterFactory(Json.asConverterFactory(MediaType.get("application/json")))
            .build()
            .create(PoeTradeApi::class.java)
    } bind PoeTradeApi::class
    single {
        CoreRepository(get(), get())
    } bind ICoreRepository::class
}