package com.poe.tradeapp.core.presentation

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxItemDecoration
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.poe.tradeapp.core.R

fun FragmentManager.addFragment(resId: Int, fragment: Fragment) {
    val visibleFragments = this.fragments.filter { it.isVisible }
    val transaction = this.beginTransaction()
    visibleFragments.forEach {
        transaction.hide(it)
    }
    transaction.add(resId, fragment)
    transaction.commit()
}

fun RecyclerView.measureForAnimator(): Int {
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

val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()
val Float.spf: Float
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this,
        Resources.getSystem().displayMetrics
    )