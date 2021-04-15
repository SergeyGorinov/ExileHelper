package com.poe.tradeapp.exchange.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.poe.tradeapp.exchange.R
import com.poe.tradeapp.exchange.databinding.FilterViewBinding

class FilterView(ctx: Context, attrs: AttributeSet) : ConstraintLayout(ctx, attrs) {

    private var viewBinding: FilterViewBinding? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.filter_view, this, true)
        viewBinding = FilterViewBinding.bind(this)

        context.theme.obtainStyledAttributes(attrs, R.styleable.FilterView, 0, 0).apply {
            try {
                viewBinding?.filterEnabled?.isChecked =
                    getBoolean(R.styleable.FilterView_android_checked, false)
                viewBinding?.filterShowHideButton?.text =
                    getString(R.styleable.FilterView_fieldName)
            } finally {
                recycle()
            }
        }
    }

    override fun onViewRemoved(view: View?) {
        super.onViewRemoved(view)
        viewBinding = null
    }
}