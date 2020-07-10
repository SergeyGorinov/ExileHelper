package com.example.poetradeapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import kotlinx.android.synthetic.main.loading_layout.*
import okhttp3.Request
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class LoadingActivity : AppCompatActivity() {

    val gson = Gson()
    val httpClient = okhttp3.OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loading_layout)

        doAsync {
            try {
                var request =
                    Request.Builder().url("https://www.pathofexile.com/api/trade/data/static")
                        .build()
                httpClient.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) throw IOException("Failed. Code: ${response.code()}")
                    val jsonData = response.body()!!.string()
                    var data = gson.fromJson<MainActivity.StaticModel>(jsonData)

                    uiThread {
                        staticLoadingPB.visibility = View.VISIBLE
                        staticLoadingPB.max = data.result.first().entries.size
                        //staticLoadingPB.max = data.result.sumBy { s -> s.entries.size }
                    }

                    data.result.first().entries.forEach { currency ->
                        try {
                            val url = URL("https://www.pathofexile.com" + currency.image)
                            val connection = url.openConnection() as HttpURLConnection
                            connection.doInput = true
                            connection.connect()
                            val imageRaw = connection.inputStream.readBytes()
                            connection.disconnect()

                            database.writableDatabase.insert("StaticData",
                                "GroupId" to data.result.first().id,
                                "GroupDescription" to data.result.first().label,
                                "ItemId" to currency.id,
                                "ItemDescription" to currency.text,
                                "ItemImage" to imageRaw
                            )

                            uiThread {
                                staticLoadingPB.incrementProgressBy(1)
                            }
                        }
                        catch (e: java.lang.Exception) {
                            Log.e("ERROR", e.message)
                        }
                    }

//                    data.result.forEach {currencyGroup ->
//
//                    }
                }
                uiThread {
                    it.startActivity(Intent(it, MainActivity::class.java))
                }
            } catch (e: Exception) {
                println(e.message)
            }
        }
    }
}