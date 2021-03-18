package com.poe.tradeapp.core.presentation

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.internal.BaselineLayout
import com.poe.tradeapp.core.R

class BottomNavigationViewCompat(context: Context, attrs: AttributeSet) :
    BottomNavigationView(context, attrs) {

    private val fontFamilyCompat = ResourcesCompat.getFont(context, R.font.fontinsmallcaps)

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        val menu = getChildAt(0) as ViewGroup
        for (i in 0 until menu.childCount) {
            val item = menu.getChildAt(i) as BottomNavigationItemView
            val itemTitle = item.getChildAt(1) as BaselineLayout
            (itemTitle.getChildAt(0) as TextView).typeface = fontFamilyCompat
            (itemTitle.getChildAt(1) as TextView).typeface = fontFamilyCompat
        }

    }
}