package com.example.poetradeapp

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.poetradeapp.models.RequestModel
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.poetradeapp.http.RequestService
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class League(
        val id: String,
        val text: String
    )

    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class LeagueModel(
        val result: List<League>
    )

    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class StaticData(
        val id: String,
        val text: String,
        val image: String
    )

    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class StaticEntries(
        val id: String,
        val label: String,
        val entries: List<StaticData>
    )

    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class StaticModel(
        val result: List<StaticEntries>
    )

    private val testArray = arrayOf("", "Belgium", "France", "Italy", "Germany", "Spain")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = ArrayAdapter(this, R.layout.custom_spinner_item, testArray)

        testAutoComplete.setAdapter(adapter)
        filled_exposed_dropdown.setAdapter(adapter)

        testButton.setOnClickListener {
            if (testLayout.visibility == View.VISIBLE) {
                val animate = TranslateAnimation(0.0f, 0.0f, 0.0f, testLayout.height.toFloat())
                animate.duration = 500
                animate.fillAfter = true
                testLayout.startAnimation(animate)
            } else {
                testLayout.visibility = View.VISIBLE
                val animate = TranslateAnimation(0.0f, 0.0f, testLayout.height.toFloat(), 0.0f)
                animate.duration = 500
                animate.fillAfter = true
                testLayout.startAnimation(animate)
            }
        }

        val test = jacksonObjectMapper()
        val serialized = test.writeValueAsString(RequestModel())

        println(serialized)

        GlobalScope.launch {
            try {
                val retrofit = RequestService.create("https://www.pathofexile.com/")
                val retroTest = retrofit.getLeagueData("api/trade/data/leagues").execute()

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
            } catch (e: Exception) {
                println(e.message)
            }
        }
    }
}
