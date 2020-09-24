package com.poetradeapp

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.poetradeapp.R
import com.poetradeapp.adapters.SwipePagerAdapter
import com.poetradeapp.fragments.item.ItemExchangeMainFragment
import com.poetradeapp.fragments.LoaderFragment
import com.poetradeapp.fragments.currency.CurrencyExchangeMainFragment
import com.poetradeapp.http.RequestService
import com.poetradeapp.models.MainViewModel
import io.realm.Realm
import io.realm.RealmList
import io.realm.RealmObject
import kotlinx.android.synthetic.main.activity_main.*
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

class MainActivity : FragmentActivity() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(this.application)
        ).get(MainViewModel::class.java)
    }

    private val currencyFragmentMain = CurrencyExchangeMainFragment()
    private val itemExchangeFragment = ItemExchangeMainFragment()
    private val loaderFragment = LoaderFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val realm = Realm.getDefaultInstance()

//        realm.beginTransaction()
//        realm.deleteAll()
//        realm.commitTransaction()

        mainContainer.offscreenPageLimit = 2

        if (realm.where(RealmCurrencyData::class.java).count() == 0L) {
            val adapter = SwipePagerAdapter(
                this,
                mutableListOf(loaderFragment, currencyFragmentMain, itemExchangeFragment)
            )
            mainContainer.adapter = adapter
            GlobalScope.launch {
                updateData()
            }.invokeOnCompletion {
                GlobalScope.launch(Dispatchers.Main) {
                    adapter.removeFragment(0)
                }
            }
        } else {
            viewModel.initializeIcons(this)
            mainContainer.adapter =
                SwipePagerAdapter(this, mutableListOf(currencyFragmentMain, itemExchangeFragment))
        }
    }

    override fun onBackPressed() {
        if (!currencyFragmentMain.currencyFragmentResult.isHidden) {
            currencyFragmentMain.hideResults()
        }
        else
            super.onBackPressed()
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
