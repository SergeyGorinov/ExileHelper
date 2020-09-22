package com.poetradeapp

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.poetradeapp.R
import com.poetradeapp.fragments.LoaderFragment
import com.poetradeapp.fragments.MainFragment
import com.poetradeapp.fragments.ResultFragment
import com.poetradeapp.http.RequestService
import com.poetradeapp.models.*
import io.realm.Realm
import io.realm.RealmObject
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.consumeEach

open class RealmTest(
    var name: String = "",
    var age: Int = 0
) : RealmObject()

class MainActivity : FragmentActivity() {

    private lateinit var retrofit: RequestService
    private lateinit var realm: Realm

    private val loaderFragment = LoaderFragment()
    private val mainFragment = MainFragment()
    private val resultFragment = ResultFragment()

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(this.application)
        ).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager
            .beginTransaction()
            .add(R.id.mainFragmentContainer, loaderFragment)
            .add(R.id.mainFragmentContainer, mainFragment)
            .add(R.id.mainFragmentContainer, resultFragment)
            .hide(mainFragment)
            .hide(resultFragment)
            .commit()

//        realm = Realm.getDefaultInstance()
//
//        realm.beginTransaction()
//        realm.deleteAll()
//        realm.commitTransaction()

        GlobalScope.launch {
            viewModel.channel.consumeEach {
                supportFragmentManager.beginTransaction()
                    .hide(mainFragment)
                    .show(resultFragment)
                    .commit()
            }
        }

        retrofit = RequestService.create("https://www.pathofexile.com/")

        GlobalScope.launch {
            updateData()
        }.invokeOnCompletion {
            GlobalScope.launch(Dispatchers.Main) {
                supportFragmentManager
                    .beginTransaction()
                    .hide(loaderFragment)
                    .show(mainFragment)
                    .commit()
            }
        }
    }

    private suspend fun updateData() {
        val data = withContext(Dispatchers.Default) {
            retrofit.getStaticData("api/trade/data/static").execute().body()?.let {
                withContext(Dispatchers.Main) {
                    loaderFragment.progressBar.max =
                        it.result.sumBy { sum -> sum.entries.count { c -> c.image != null } }
                }
                it.result.forEach { currencyGroup ->
                    currencyGroup.entries.forEach { currency ->
                        currency.image?.let { link ->
                            val rawImage = retrofit.getStaticImage(link).execute().body()?.bytes()
                            rawImage?.let { imageData ->
                                currency.drawable = BitmapDrawable(
                                    resources,
                                    BitmapFactory.decodeByteArray(imageData, 0, imageData.size)
                                )
                                withContext(Dispatchers.Main) {
                                    loaderFragment.progressBar.incrementProgressBy(1)
                                }
                            }
                        }
                    }
                }
                it.result
            }
        }
        data?.let { viewModel.setMainData(data) } ?: viewModel.setMainData(listOf())
    }
}
