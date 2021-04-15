package com.poe.tradeapp.exchange.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.poe.tradeapp.exchange.R
import com.poe.tradeapp.exchange.databinding.AccountViewBinding

class AccountView(ctx: Context, attrs: AttributeSet) : ConstraintLayout(ctx, attrs) {

    val viewBinding: AccountViewBinding

    init {
        val view = LayoutInflater.from(ctx).inflate(R.layout.account_view, this, true)
        viewBinding = AccountViewBinding.bind(view)

        context.theme.obtainStyledAttributes(attrs, R.styleable.FilterView, 0, 0).apply {
            try {
                viewBinding.filterName.text = getString(R.styleable.FilterView_fieldName)
            } finally {
                recycle()
            }
        }
    }

}