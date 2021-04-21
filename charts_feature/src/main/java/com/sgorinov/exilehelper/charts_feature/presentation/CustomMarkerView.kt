package com.sgorinov.exilehelper.charts_feature.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.sgorinov.exilehelper.charts_feature.R

@SuppressLint("ViewConstructor")
internal class CustomMarkerView(context: Context, resId: Int) : MarkerView(context, resId) {

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        e?.y?.let {
            findViewById<TextView>(R.id.tvContent).text = String.format("%.1f", it)
        }
        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        val xOffset = -(width / 2)
        val yOffset = -height - 10
        return MPPointF(xOffset.toFloat(), yOffset.toFloat())
    }
}