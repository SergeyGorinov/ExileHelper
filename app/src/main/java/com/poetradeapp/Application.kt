package com.poetradeapp

import android.app.Application
import android.content.Context
import com.poetradeapp.activities.CurrencyExchangeActivity
import com.poetradeapp.activities.ItemsSearchActivity
import com.poetradeapp.adapters.CurrencyGroupViewHolder
import com.poetradeapp.fragments.LoaderFragment
import com.poetradeapp.fragments.MainFragment
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ContextProvider constructor(private val context: Context) {
    @Provides
    @Singleton
    fun context(): Context {
        return context
    }
}

@Module
class BaseUriProvider {
    @Provides
    @Singleton
    fun baseUri(): String {
        return "https://www.pathofexile.com/"
    }
}

@Component(modules = [ContextProvider::class, BaseUriProvider::class])
@Singleton
interface ApplicationComponent {
    fun inject(target: LoaderFragment)
    fun inject(target: MainFragment)
    fun inject(target: CurrencyExchangeActivity)
    fun inject(target: ItemsSearchActivity)
    fun inject(target: CurrencyGroupViewHolder)
}

class PoeTradeApplication : Application() {

    private lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        applicationComponent = DaggerApplicationComponent
            .builder()
            .contextProvider(ContextProvider(this))
            .build()
    }

    fun getDaggerComponent() = applicationComponent
}