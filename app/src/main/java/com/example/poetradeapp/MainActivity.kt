package com.example.poetradeapp

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.poetradeapp.models.RequestModel
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.android.synthetic.main.activity_main.*

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

    private val testArray = arrayOf("", "Belgium", "France", "Italy", "Germany", "Spain")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = ArrayAdapter(this, R.layout.custom_spinner_item, testArray)

        testAutoComplete.setAdapter(adapter)
        filled_exposed_dropdown.setAdapter(adapter)

        val test = jacksonObjectMapper()
        val serialized = test.writeValueAsString(RequestModel())

        println(serialized)

//        doAsync {
//            try {
//                var request = Request.Builder().url("https://www.pathofexile.com/api/trade/data/leagues").build()
//                httpClient.newCall(request).execute().use { response ->
//                    if (!response.isSuccessful) throw IOException("Failed. Code: ${response.code()}")
//                    var data = gson.fromJson<LeagueModel>(response.body()!!.string())
//                    uiThread {
//                        try {
//                            val adapter = CustomSpinnerAdapter(data.result, it)
//                            LeagueList.adapter = adapter
//                            LeagueList.onItemSelectedListener = adapter
//                        }
//                        catch (e: java.lang.Exception) {
//                            println(e)
//                        }
//                    }
//                }
//                request = Request.Builder().url("https://www.pathofexile.com/api/trade/data/static").build()
//
//                httpClient.newCall(request).execute().use {response ->
//                    if (!response.isSuccessful) throw IOException("Failed. Code: ${response.code()}")
//                    val jsonData = response.body()!!.string()
//                    var data = gson.fromJson<StaticModel>(jsonData)
//                    uiThread {
//                        try {
//                            var items: Array<StaticData> = arrayOf(StaticData())
//                            data.result.forEach {
//                                items += it
//                            }
//                            StaticItemsList.adapter = CustomListViewAdapter(items, it)
//                            setListViewHeightBasedOnChildren(StaticItemsList)
//                        }
//                        catch (e: java.lang.Exception) {
//                            println(e)
//                        }
//                    }
//                }
//            }
//            catch (e: Exception) {
//                println(e.message)
//            }
//        }
    }
}
