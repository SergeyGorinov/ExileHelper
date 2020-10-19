package com.poetradeapp.ui

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.style.ImageSpan
import java.lang.ref.WeakReference

class CenteredImageSpan(drawable: Drawable) : ImageSpan(drawable) {

    private var wrDrawable: WeakReference<Drawable>? = null

    override fun getSize(
        paint: Paint,
        text: CharSequence?,
        start: Int,
        end: Int,
        fm: Paint.FontMetricsInt?
    ): Int {
        val d = getCachedDrawable()
        val rect = d.bounds

        fm?.let {
            val pfm = paint.fontMetricsInt
            fm.ascent = pfm.ascent
            fm.descent = pfm.descent
            fm.top = pfm.top
            fm.bottom = pfm.bottom
        }

        return rect.right
    }

    override fun draw(
        canvas: Canvas,
        text: CharSequence?,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint
    ) {
        val d = getCachedDrawable()
        canvas.save()

        val drawableHeight = d.intrinsicHeight
        val fontAscent = paint.fontMetricsInt.ascent
        val fontDescent = paint.fontMetricsInt.descent
        val translateY = bottom - d.bounds.bottom + (drawableHeight - fontDescent + fontAscent) / 2
        canvas.translate(x, translateY.toFloat())
        d.draw(canvas)
        canvas.restore()
    }

    private fun getCachedDrawable(): Drawable {
        val wrd = wrDrawable
        var d: Drawable? = null

        if (wrd != null)
            d = wrd.get()
        if (d == null) {
            d = drawable
            wrDrawable = WeakReference(d)
        }

        return d as Drawable
    }
}