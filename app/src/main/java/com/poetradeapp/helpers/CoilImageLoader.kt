package com.poetradeapp.helpers

import android.content.Context
import coil.ImageLoader
import coil.util.CoilUtils
import okhttp3.OkHttpClient
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CoilImageLoader {

    private val imageLoader: ImageLoader

    @Inject
    constructor(context: Context) {
        imageLoader = ImageLoader.Builder(context)
            .okHttpClient {
                OkHttpClient.Builder()
                    .cache(CoilUtils.createDefaultCache(context))
                    .build()
            }
            .build()
    }

    fun getImageLoader() = imageLoader
}