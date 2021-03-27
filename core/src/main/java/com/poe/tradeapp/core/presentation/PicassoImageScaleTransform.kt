package com.poe.tradeapp.core.presentation

import android.graphics.Bitmap
import com.squareup.picasso.Transformation

class PicassoImageScaleTransform : Transformation {
    override fun transform(source: Bitmap): Bitmap {
        val scaledBitmap =
            Bitmap.createScaledBitmap(source, source.width.dp, source.height.dp, true)
        source.recycle()
        return scaledBitmap
    }

    override fun key(): String {
        return "scaleToDpi()"
    }
}