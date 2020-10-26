package com.poetradeapp.http

import com.poetradeapp.models.requestmodels.ExchangeCurrencyRequestModel
import com.poetradeapp.models.requestmodels.ItemRequestModel
import com.poetradeapp.models.responsemodels.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

@ExperimentalCoroutinesApi
interface RequestService {

    @GET
    fun getLeagueData(@Url url: String): Call<LeaguesModel>

    @GET
    fun getItemsData(@Url url: String): Call<SearchItemsModel>

    @GET
    fun getStatsData(@Url url: String): Call<StatsModel>

    @GET
    fun getStaticData(@Url url: String): Call<StaticModel>

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