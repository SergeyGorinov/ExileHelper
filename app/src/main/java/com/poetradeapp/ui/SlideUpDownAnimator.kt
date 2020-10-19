package com.poetradeapp.ui

import android.animation.ObjectAnimator
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

class SlideUpDownAnimator(private val view: View) {

    private var preMeasuredHeight = 0

    fun slideDown(searchField: LinearLayout? = null) {
        view.visibility = View.VISIBLE

        val height = preMeasuredHeight
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

    fun slideUp(searchField: LinearLayout? = null) {
        view.post(kotlinx.coroutines.Runnable {
            preMeasuredHeight = view.height
            val valueAnimator = ObjectAnimator.ofInt(preMeasuredHeight, 0)
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

    fun setHeight(h: Int) {
        preMeasuredHeight = h
    }
}