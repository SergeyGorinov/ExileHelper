package com.poetradeapp

import android.app.Application
import androidx.room.Room
import coil.ImageLoader
import coil.util.CoilUtils
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.poetradeapp.dl.DatabaseRepository
import com.poetradeapp.http.RequestService
import com.poetradeapp.ui.SocketsTemplateLoader
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

@ExperimentalCoroutinesApi
class PoeTradeApplication : Application() {
    private val coilModule = module {
        single {
            OkHttpClient.Builder()
                .cache(CoilUtils.createDefaultCache(androidContext()))
                .build()
        }
        single {
            ImageLoader
                .Builder(androidContext())
                .okHttpClient { get() }
                .build()
        }
    }

    private val retrofitModule = module {
        factory {
            Retrofit.Builder()
                .baseUrl("https://www.pathofexile.com/")
                .addConverterFactory(JacksonConverterFactory.create(jacksonObjectMapper()))
                .build()
                .create(RequestService::class.java) as RequestService
        }
    }

    private val socketTemplateModule = module {
        single {
            SocketsTemplateLoader(androidContext())
        }
    }

    private val RepositoryModule = module {
        factory {
            Room.databaseBuilder(
                androidContext(),
                DatabaseRepository::class.java,
                "data.db"
            ).build()
        }
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@PoeTradeApplication)
            modules(listOf(coilModule, retrofitModule, socketTemplateModule, RepositoryModule))
        }
    }
}