package com.poetradeapp.ui

import android.animation.ObjectAnimator
import android.content.res.Resources
import android.view.View
import android.view.ViewGroup
import android.widget.GridView

class SlideUpDownAnimator(private val view: View) {

    fun slideDown() {
        view.visibility = View.VISIBLE
        val layoutParams = view.layoutParams
        layoutParams.height = 1
        view.layoutParams = layoutParams

        view.measure(
            View.MeasureSpec.makeMeasureSpec(
                Resources.getSystem().displayMetrics.widthPixels,
                View.MeasureSpec.EXACTLY
            ),
            View.MeasureSpec.makeMeasureSpec(GridView.MEASURED_SIZE_MASK, View.MeasureSpec.AT_MOST)
        )

        val height = view.measuredHeight
        val valueAnimator = ObjectAnimator.ofInt(1, height)
        valueAnimator.addUpdateListener { animation ->
            val value = animation?.animatedValue as Int
            if (height > value) {
                val layoutParams = view.layoutParams
                layoutParams.height = value
                view.layoutParams = layoutParams
            } else {
                val layoutParams = view.layoutParams
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                view.layoutParams = layoutParams
            }
        }
        valueAnimator.duration = 500L
        valueAnimator.start()
    }

    fun slideUp() {
        view.post(kotlinx.coroutines.Runnable {
            val height = view.height
            val valueAnimator = ObjectAnimator.ofInt(height, 0)
            valueAnimator.addUpdateListener { animation ->
                val value = animation?.animatedValue as Int
                if (value > 0) {
                    val layoutParams = view.layoutParams
                    layoutParams.height = value
                    view.layoutParams = layoutParams
                } else {
                    view.visibility = View.GONE
                }
            }
            valueAnimator.duration = 500L
            valueAnimator.start()
        })
    }
}