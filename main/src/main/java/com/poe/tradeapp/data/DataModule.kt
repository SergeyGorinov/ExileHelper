package com.poe.tradeapp.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.poe.tradeapp.data.repository.MainRepository
import com.poe.tradeapp.domain.IMainRepository
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit

internal val dataModule = module {
    single {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/")
            .addConverterFactory(Json.asConverterFactory(MediaType.get("application/json")))
            .build()
            .create(PoeTradeApi::class.java) as PoeTradeApi
    }
    single { MainRepository(get()) } bind IMainRepository::class
}