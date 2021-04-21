package com.sgorinov.exilehelper.core.presentation

import android.animation.ValueAnimator
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class SlideUpDownAnimator(private val view: View) {

    private var preMeasuredHeight = 0

    fun slideDown() {
        view.visibility = View.VISIBLE
        if (view is RecyclerView) {
            view.descendantFocusability = ViewGroup.FOCUS_BEFORE_DESCENDANTS
        }
        val valueAnimator = ValueAnimator.ofInt(1, preMeasuredHeight)
        valueAnimator.addUpdateListener { animation ->
            val value = animation?.animatedValue as Int
            if (preMeasuredHeight > value) {
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
        view.post {
            preMeasuredHeight = view.height
            val valueAnimator = ValueAnimator.ofInt(preMeasuredHeight, 0)
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
        }
    }

    fun setHeight(h: Int) {
        preMeasuredHeight = h
    }
}