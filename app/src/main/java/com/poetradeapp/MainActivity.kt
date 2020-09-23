package com.poetradeapp

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.poetradeapp.R
import com.poetradeapp.fragments.LoaderFragment
import com.poetradeapp.fragments.MainFragment
import com.poetradeapp.fragments.ResultFragment
import com.poetradeapp.http.RequestService
import com.poetradeapp.models.MainViewModel
import io.realm.Realm
import io.realm.RealmList
import io.realm.RealmObject
import kotlinx.coroutines.*

open class RealmCurrencyData(
    var id: String = "",
    var label: String = "",
    var image: ByteArray? = null
) : RealmObject()

open class RealmCurrencyGroupData(
    var id: String = "",
    var label: String? = "",
    var currencies: RealmList<RealmCurrencyData> = RealmList()
) : RealmObject()

interface FragmentChanger {
    fun changeFragment()
}

class MainActivity : FragmentActivity(), FragmentChanger {
    private val loaderFragment = LoaderFragment()
    private val mainFragment = MainFragment()
    private val resultFragment = ResultFragment()

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(this.application)
        ).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val realm = Realm.getDefaultInstance()

//        realm.beginTransaction()
//        realm.deleteAll()
//        realm.commitTransaction()

        if (realm.where(RealmCurrencyData::class.java).count() == 0L) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.mainFragmentContainer, loaderFragment)
                .show(loaderFragment)
                .commit()
            GlobalScope.launch {
                updateData()
            }.invokeOnCompletion {
                GlobalScope.launch(Dispatchers.Main) {
                    supportFragmentManager
                        .beginTransaction()
                        .remove(loaderFragment)
                        .add(R.id.mainFragmentContainer, mainFragment)
                        .add(R.id.mainFragmentContainer, resultFragment)
                        .show(mainFragment)
                        .commit()
                }
            }
        } else {
            supportFragmentManager
                .beginTransaction()
                .remove(loaderFragment)
                .add(R.id.mainFragmentContainer, mainFragment)
                .add(R.id.mainFragmentContainer, resultFragment)
                .hide(resultFragment)
                .show(mainFragment)
                .commit()
        }
    }

    override fun onBackPressed() {
        if (!resultFragment.isHidden) {
            supportFragmentManager
                .beginTransaction()
                .hide(resultFragment)
                .show(mainFragment)
                .commit()
        }
        else {
            super.onBackPressed()
        }
    }

    override fun changeFragment() {
        supportFragmentManager
            .beginTransaction()
            .hide(mainFragment)
            .show(resultFragment)
            .commit()
    }

    private suspend fun updateData() {
        val retrofit = RequestService.create("https://www.pathofexile.com/")
        retrofit.getStaticData("api/trade/data/static").execute().body()?.let { data ->
            withContext(Dispatchers.Main) {
                loaderFragment.progressBar.max =
                    data.result.sumBy { sum -> sum.entries.count { c -> c.image != null } }
            }
            Realm.getDefaultInstance().executeTransaction { bgRealm ->
                data.result.forEach { currencyGroup ->
                    val realmCurrencyGroup =
                        bgRealm.createObject(RealmCurrencyGroupData::class.java)
                    realmCurrencyGroup.id = currencyGroup.id
                    realmCurrencyGroup.label = currencyGroup.label
                    currencyGroup.entries.forEach { currency ->
                        val realmCurrency = bgRealm.createObject(RealmCurrencyData::class.java)
                        currency.image?.let { link ->
                            realmCurrency.image =
                                retrofit.getStaticImage(link).execute().body()?.bytes()
                            GlobalScope.launch(Dispatchers.Main) {
                                loaderFragment.progressBar.incrementProgressBy(1)
                            }
                        }
                        realmCurrency.id = currency.id
                        realmCurrency.label = currency.text
                        realmCurrencyGroup.currencies.add(realmCurrency)
                    }
                }
            }
        }
        viewModel.initializeIcons(this)
    }
}
