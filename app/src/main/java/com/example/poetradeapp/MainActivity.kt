package com.example.poetradeapp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    data class League(
        val id: String,
        val text: String
    )

    data class LeagueModel(
        val result: Array<League>
    )

    data class StaticData(
        val id: String,
        val text: String,
        val image: String
    )

    data class StaticEntries(
        val id: String,
        val label: String,
        val entries: Array<StaticData>
    )

    data class StaticModel(
        val result: Array<StaticEntries>
    )

    class CurrencyGroup(val id: String, val description: String, val currencies: List<Currency>) :
        ExpandableGroup<Currency>(id, currencies)

    @Parcelize
    class Currency(
        val id: String,
        val text: String,
        val image: Bitmap
    ) : Parcelable

    private val httpClient = OkHttpClient()
    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        CurrencyGroupList.layoutManager = LinearLayoutManager(this)

        CurrencyGroupList.adapter = CurrencyGroupListAdapter(mutableListOf<ExpandableGroup<*>?>())

        doAsync {
            try {
                var request =
                    Request.Builder().url("https://www.pathofexile.com/api/trade/data/leagues")
                        .build()
                httpClient.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) throw IOException("Failed. Code: ${response.code()}")
                    var data = gson.fromJson<LeagueModel>(response.body()!!.string())
                    uiThread {
                        try {
                            val adapter = CustomSpinnerAdapter(data.result, it)
                            LeagueList.adapter = adapter
                            LeagueList.onItemSelectedListener = adapter
                        } catch (e: java.lang.Exception) {
                            println(e)
                        }
                    }
                }

                request = Request.Builder().url("https://www.pathofexile.com/api/trade/data/static")
                    .build()
                httpClient.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) throw IOException("Failed. Code: ${response.code()}")
                    val jsonData = response.body()!!.string()
                    var data = gson.fromJson<StaticModel>(jsonData)

                    data.result.forEach { currencyGroup ->
                        var currencies = listOf<Currency>()
                        currencyGroup.entries.forEach { currency ->
                            val url = URL("https://www.pathofexile.com" + currency.image)
                            val connection = url.openConnection() as HttpURLConnection
                            connection.doInput = true
                            connection.connect()
                            val imageStream = connection.inputStream
                            val image = BitmapFactory.decodeStream(imageStream)
                            currencies += Currency(currency.id, currency.text, image)
                            connection.disconnect()
                        }
                        var group = CurrencyGroup(
                            currencyGroup.id,
                            currencyGroup.label,
                            currencies
                        )
                        uiThread {
                            (CurrencyGroupList.adapter as CurrencyGroupListAdapter).addData(group)
                        }
                    }
                }
            } catch (e: Exception) {
                println(e.message)
            }
        }
    }

    private fun setListViewHeightBasedOnChildren(listView: ListView) {
        val adapter: ListAdapter = listView.adapter ?: return
        var totalHeight = 0
        for (i in 0 until adapter.count) {
            val listItem: View = adapter.getView(i, null, listView)
            listItem.measure(0, 0)
            totalHeight += listItem.measuredHeight
        }
        val parameters: ViewGroup.LayoutParams = listView.layoutParams
        parameters.height = totalHeight + listView.dividerHeight * (adapter.count - 1)
        listView.layoutParams = parameters
    }
}
