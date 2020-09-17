package com.example.poetradeapp

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.poetradeapp.adapters.CurrencyGridViewAdapter
import com.poetradeapp.http.RequestService
import com.poetradeapp.models.StaticData
import com.poetradeapp.ui.SlideUpDownAnimator
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var retrofit: RequestService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        retrofit = RequestService.create("https://www.pathofexile.com/")

        val animator = SlideUpDownAnimator(testGridLayout)

        val staticData = runBlocking {
            getStaticData()
        }

        testButton.setOnClickListener {
            if (testGridLayout.visibility == View.VISIBLE)
                animator.slideUp()
            else
                animator.slideDown()
        }

        testGridView.adapter = CurrencyGridViewAdapter(staticData, layoutInflater)
    }

    private suspend fun getStaticData() = run {
        withContext(Dispatchers.Default) {
            val data = retrofit.getStaticData("api/trade/data/static").execute()
                .body()?.result?.first()?.entries as List<StaticData>
            data.forEach { entry ->
                val byteImage = entry.image?.let { link ->
                    retrofit.getStaticImage(link).execute().body()?.bytes()
                }
                byteImage?.let {
                    entry.drawable =
                        BitmapDrawable(resources, BitmapFactory.decodeByteArray(it, 0, it.size))
                }
            }
            data
        }
    }
}
