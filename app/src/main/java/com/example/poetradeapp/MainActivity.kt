package com.example.poetradeapp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.Request
import org.jetbrains.anko.db.select
import org.jetbrains.anko.db.transaction
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.IOException

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

    class CurrencyGroup(val id: String, val description: String?, val currencies: List<Currency>) :
        ExpandableGroup<Currency>(id, currencies)

    @Parcelize
    class Currency(
        val id: String,
        val text: String,
        val image: Bitmap
    ) : Parcelable

    val gson = Gson()
    val httpClient = okhttp3.OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        doAsync {
            var request =
                Request.Builder().url("https://www.pathofexile.com/api/trade/data/leagues")
                    .build()
            httpClient.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Failed. Code: ${response.code()}")
                var data = gson.fromJson<MainActivity.LeagueModel>(response.body()!!.string())
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
        }

        var currencies = listOf<Currency>()

        var groups = mutableListOf<ExpandableGroup<*>?>()

        database.use {
            transaction {

            }
        }

        try {
            val dbData = database.readableDatabase.select("StaticData").parseList(DataParser())
            dbData.forEach {
                currencies += Currency(it.ItemId, it.ItemDescription ?: "Unknown", BitmapFactory.decodeByteArray(it.ItemImagePath, 0, it.ItemImagePath.size))
            }
            groups.add(CurrencyGroup(dbData.first().GroupId, dbData.first().GroupDescription, currencies))

            CurrencyGroupList.layoutManager = LinearLayoutManager(this)
            CurrencyGroupList.adapter = CurrencyGroupListAdapter(groups)
        }
        catch (e: Exception) {
            Log.e("ERROR", e.message)
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
