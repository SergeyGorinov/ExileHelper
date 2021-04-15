package com.poe.tradeapp.core.presentation

import android.app.AlertDialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxItemDecoration
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.tabs.TabLayout
import com.poe.tradeapp.core.R
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.java.KoinJavaComponent.getKoin
import java.util.*

fun View.measureForAnimator(): Int {
    val layoutParams = this.layoutParams
    layoutParams.height = 1
    this.layoutParams = layoutParams

    this.measure(
        View.MeasureSpec.makeMeasureSpec(
            Resources.getSystem().displayMetrics.widthPixels,
            View.MeasureSpec.EXACTLY
        ),
        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    )

    return this.measuredHeight
}

fun Context.hideKeyboard(view: View) {
    val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Context.generateLinearDividerDecoration(drawableResId: Int = R.drawable.recyclerview_divider): DividerItemDecoration {
    return DividerItemDecoration(this, DividerItemDecoration.VERTICAL).apply {
        ContextCompat.getDrawable(
            this@generateLinearDividerDecoration, drawableResId
        )?.let {
            setDrawable(it)
        }
    }
}

fun Context.generateCustomDividerDecoration(
    drawableResId: Int,
    leftOffset: Int
): RecyclerView.ItemDecoration {
    val divider = ContextCompat.getDrawable(this, drawableResId)
    return object : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            outRect.bottom = (divider?.intrinsicHeight ?: 0)
        }

        override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
            val left = parent.paddingLeft + leftOffset
            val right = parent.width - parent.paddingRight
            for (i in 0 until parent.childCount - 1) {
                val child = parent.getChildAt(i)
                val top = child.bottom
                val bottom = top + (divider?.intrinsicHeight ?: 0)
                divider?.setBounds(left, top, right, bottom)
                divider?.draw(c)
            }
        }
    }
}

fun Context.generateFlexboxManager(): FlexboxLayoutManager {
    return FlexboxLayoutManager(this).apply {
        flexWrap = FlexWrap.WRAP
        justifyContent = JustifyContent.CENTER
    }
}

fun Context.generateFlexboxDecorator(): FlexboxItemDecoration {
    return FlexboxItemDecoration(this).apply {
        setDrawable(
            ContextCompat.getDrawable(
                this@generateFlexboxDecorator,
                R.drawable.recyclerview_divider
            )
        )
    }
}

fun Context.getTransparentProgressDialog(): AlertDialog {
    return AlertDialog.Builder(this).setView(
        View.inflate(
            this,
            R.layout.progress_dialog,
            null
        )
    ).create().apply {
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(false)
    }
}

fun String.toLowerCaseLocalized() = this.toLowerCase(Locale.getDefault())

inline fun <reified T : ViewModel> scopedViewModel(
    scopeId: String,
    scopeName: FragmentScopes
): Lazy<T> {
    return lazy {
        val scope = getKoin().getOrCreateScope(scopeId, named(scopeName))
        scope.get()
    }
}

fun fragmentLifecycleScope(scopeId: String, scopeName: FragmentScopes): Lazy<Scope> {
    return lazy { getKoin().getOrCreateScope(scopeId, named(scopeName)) }
}

fun TabLayout.changeTabsFont(font: Typeface?) {
    font ?: return
    val vg = getChildAt(0) as ViewGroup
    val tabsCount = vg.childCount
    for (j in 0 until tabsCount) {
        val vgTab = vg.getChildAt(j) as ViewGroup
        val tabChildCount = vgTab.childCount
        for (i in 0 until tabChildCount) {
            val tabViewChild = vgTab.getChildAt(i)
            if (tabViewChild is TextView) {
                tabViewChild.typeface = font
            }
        }
    }
}

val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()
val Int.dpf: Float
    get() = this * Resources.getSystem().displayMetrics.density + 0.5f