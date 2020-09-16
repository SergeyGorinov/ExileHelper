package com.example.poetradeapp

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.poetradeapp.models.RequestModel
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.poetradeapp.adapters.CurrencyGridViewAdapter
import com.poetradeapp.http.RequestService
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    @JsonInclude
    data class League(
        val id: String,
        val text: String
    )

    @JsonInclude
    data class LeagueModel(
        val result: List<League>
    )

    @JsonInclude
    data class StaticData(
        val id: String,
        val text: String,
        val image: String?
    )

    @JsonInclude
    data class StaticEntries(
        val id: String,
        val label: String?,
        val entries: List<StaticData>
    )

    @JsonInclude
    data class StaticModel(
        val result: List<StaticEntries>
    )

    private val testArray = arrayOf("", "Belgium", "France", "Italy", "Germany", "Spain")

    private val customArray = listOf(
        "Java", "CSS3", "Android", "jQuery", "PHP", "MySpace",
        "HTML5", "Javascript", "MySQL", "Python", "Swift",
        "WordPress", "Facebook", "Youtube", "Twitter"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val staticData = runBlocking { getStaticData() }
        testGridView.adapter = CurrencyGridViewAdapter(staticData, layoutInflater)
    }

    private suspend fun getStaticData() = run {
        val retrofit = RequestService.create("https://www.pathofexile.com/")
        //val retroTest = retrofit.getLeagueData("api/trade/data/leagues").execute()
        withContext(Dispatchers.Default) {
            retrofit.getStaticData("api/trade/data/static").execute()
                .body()?.result?.first()?.entries as List<StaticData>
        }
    }
}
