package com.sgorinov.exilehelper.exchange.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.sgorinov.exilehelper.exchange.R
import com.sgorinov.exilehelper.exchange.databinding.FilterHeaderViewBinding

class FilterHeaderView(ctx: Context, attrs: AttributeSet) : ConstraintLayout(ctx, attrs) {

    var viewBinding: FilterHeaderViewBinding? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.filter_header_view, this, true)
        viewBinding = FilterHeaderViewBinding.bind(this)

        context.theme.obtainStyledAttributes(attrs, R.styleable.ConstraintLayout, 0, 0).apply {
            try {
                viewBinding?.filterEnabled?.isChecked =
                    getBoolean(R.styleable.ConstraintLayout_android_checked, false)
                viewBinding?.filterShowHideButton?.text =
                    getString(R.styleable.ConstraintLayout_fieldName)
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