package com.poe.tradeapp.core.presentation

import android.app.AlertDialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxItemDecoration
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.poe.tradeapp.core.R
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.java.KoinJavaComponent
import org.koin.java.KoinJavaComponent.getKoin
import java.util.*

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

fun Context.generateLinearDividerDecoration(): DividerItemDecoration {
    return DividerItemDecoration(this, DividerItemDecoration.VERTICAL).apply {
        ContextCompat.getDrawable(
            this@generateLinearDividerDecoration, R.drawable.recyclerview_divider
        )?.let {
            setDrawable(it)
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

inline fun <reified T : ViewModel> Fragment.scopedViewModel(
    scopeId: String,
    scopeName: FragmentScopes
): Lazy<T> {
    return lazy {
        val scope = KoinJavaComponent.getKoin().getOrCreateScope(scopeId, named(scopeName))
        scope.get()
    }
}

fun LifecycleOwner.fragmentLifecycleScope(scopeId: String, scopeName: FragmentScopes): Lazy<Scope> {
    return lazy { getKoin().getOrCreateScope(scopeId, named(scopeName)) }
}

val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()