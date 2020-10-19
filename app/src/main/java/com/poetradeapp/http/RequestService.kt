package com.poetradeapp.http

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.poetradeapp.models.*
import com.poetradeapp.models.requestmodels.ExchangeCurrencyRequestModel
import com.poetradeapp.models.requestmodels.ItemRequestModel
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RequestService @Inject constructor(baseUrl: String) {
    private val requestService: RequestInstance = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(JacksonConverterFactory.create(jacksonObjectMapper()))
        .build()
        .create(RequestInstance::class.java)

    fun getService() = requestService
}

interface RequestInstance {

    @GET
    fun getLeagueData(@Url url: String): Call<GetLeaguesModel>

    @GET
    fun getItemsData(@Url url: String): Call<GetItemsModel>

    @GET
    fun getStatsData(@Url url: String): Call<GetStatsModel>

    @GET
    fun getCurrencyData(@Url url: String): Call<GetCurrenciesModel>

    @POST
    fun getCurrencyExchangeList(
        @Url url: String,
        @Body body: ExchangeCurrencyRequestModel
    ): Call<ItemsListResponseModel>

    @POST
    fun getItemsExchangeList(
        @Url url: String,
        @Body body: ItemRequestModel
    ): Call<ItemsListResponseModel>

    @GET
    fun getCurrencyExchangeResponse(@Url url: String): Call<ExchangeCurrencyResponse>

    @GET
    fun getItemExchangeResponse(@Url url: String): Call<ExchangeItemsResponse>
}