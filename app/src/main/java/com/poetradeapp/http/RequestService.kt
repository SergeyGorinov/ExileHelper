package com.poetradeapp.http

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.poetradeapp.models.LeagueModel
import com.poetradeapp.models.StaticModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

interface RequestService {
    companion object {
        fun create(baseUrl: String) =
            Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(JacksonConverterFactory.create(jacksonObjectMapper()))
                .build()
                .create(RequestService::class.java)
    }

    @GET
    fun getLeagueData(@Url url: String): Call<LeagueModel>

    @GET
    fun getStaticData(@Url url: String): Call<StaticModel>

    @GET
    @Streaming
    fun getStaticImage(@Url url: String): Call<ResponseBody>
}