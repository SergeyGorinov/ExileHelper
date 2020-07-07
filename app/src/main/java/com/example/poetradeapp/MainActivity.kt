package com.example.poetradeapp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    data class League (
        val id: String,
        val text: String
    )

    data class LeagueModel (
        val result: Array<League>
    )

    data class StaticData (
        val id: String,
        val text: String,
        val image: String
    )

    data class StaticEntries (
        val id: String,
        val label: String,
        val entries: Array<StaticData>
    )

    data class StaticModel (
        val result: Array<StaticEntries>
    )

    class ViewModel (
        val id: String,
        val text: String,
        val image: Bitmap
    )

    private val httpClient = OkHttpClient()
    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var items: Array<ViewModel> = arrayOf()

        doAsync {
            try {
                var request = Request.Builder().url("https://www.pathofexile.com/api/trade/data/leagues").build()
                httpClient.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) throw IOException("Failed. Code: ${response.code()}")
                    var data = gson.fromJson<LeagueModel>(response.body()!!.string())
                    uiThread {
                        try {
                            val adapter = CustomSpinnerAdapter(data.result, it)
                            LeagueList.adapter = adapter
                            LeagueList.onItemSelectedListener = adapter
                        }
                        catch (e: java.lang.Exception) {
                            println(e)
                        }
                    }
                }

                request = Request.Builder().url("https://www.pathofexile.com/api/trade/data/static").build()
                httpClient.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) throw IOException("Failed. Code: ${response.code()}")
                    val jsonData = response.body()!!.string()
                    var data = gson.fromJson<StaticModel>(jsonData)

                    data.result.forEach { MainEntry ->
                        MainEntry.entries.forEach { Entry ->
                            val url = URL("https://www.pathofexile.com" + Entry.image)
                            val connection = url.openConnection() as HttpURLConnection
                            connection.doInput = true
                            connection.connect()
                            val imageStream = connection.inputStream
                            val image = BitmapFactory.decodeStream(imageStream)
                            items += ViewModel(Entry.id, Entry.text, image)
                            connection.disconnect()
                        }
                    }
                    uiThread {
                        StaticItemsList.adapter = CustomListViewAdapter(items, it)
                    }
                }
            }
            catch (e: Exception) {
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
