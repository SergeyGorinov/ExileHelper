package com.sgorinov.exilehelper.core.presentation

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.animation.doOnEnd
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout

class TabLayoutListener(
    private val container: FrameLayout,
    private val recyclerView: RecyclerView,
    private val emptyPlaceholder: AppCompatTextView,
    private val onEndAction: (Int) -> Unit
) : TabLayout.OnTabSelectedListener {

    override fun onTabSelected(tab: TabLayout.Tab?) {
        tab ?: return
        val width = container.width.toFloat()
        when (tab.position) {
            0 -> {
                val firstAnim = slideAnimation(0f, width, tab.position, true)
                val secondAnim = slideAnimation(-width, 0f, tab.position, false)
                AnimatorSet().apply {
                    duration = 200L
                    playSequentially(firstAnim, secondAnim)
                }.start()
            }
            1 -> {
                val firstAnim = slideAnimation(0f, -width, tab.position, true)
                val secondAnim = slideAnimation(width, 0f, tab.position, false)
                AnimatorSet().apply {
                    duration = 200L
                    playSequentially(firstAnim, secondAnim)
                }.start()
            }
        }
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) = Unit

    override fun onTabReselected(tab: TabLayout.Tab?) = Unit

    private fun slideAnimation(
        from: Float,
        to: Float,
        pos: Int,
        withEnd: Boolean
    ): ValueAnimator {
        return ObjectAnimator.ofFloat(from, to).apply {
            addUpdateListener {
                val value = it.animatedValue as Float
                recyclerView.translationX = value
                emptyPlaceholder.translationX = value
            }
            if (withEnd) {
                doOnEnd {
                    onEndAction(pos)
                    recyclerView.translationX = -to
                    emptyPlaceholder.translationX = -to
                }
            }
        }
    }
}