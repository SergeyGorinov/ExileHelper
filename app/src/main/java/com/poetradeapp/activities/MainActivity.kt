package com.poetradeapp.activities

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.poetradeapp.R
import com.poetradeapp.fragments.LoaderFragment
import com.poetradeapp.fragments.MainFragment

class MainActivity : FragmentActivity() {

    private val mainFragment = MainFragment()
    private val loaderFragment = LoaderFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager
            .beginTransaction()
            .add(R.id.mainContainer, loaderFragment)
            .commit()
    }

    fun closeLoader() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainContainer, mainFragment)
            .commit()
    }
}
