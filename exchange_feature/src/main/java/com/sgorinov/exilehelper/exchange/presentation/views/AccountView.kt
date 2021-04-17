package com.sgorinov.exilehelper.exchange.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.doOnTextChanged
import com.sgorinov.exilehelper.exchange.R
import com.sgorinov.exilehelper.exchange.data.models.Field
import com.sgorinov.exilehelper.exchange.databinding.AccountViewBinding

internal class AccountView(ctx: Context, attrs: AttributeSet) : ConstraintLayout(ctx, attrs) {

    var viewBinding: AccountViewBinding?

    init {
        val view = LayoutInflater.from(ctx).inflate(R.layout.account_view, this, true)
        viewBinding = AccountViewBinding.bind(view)

        context.theme.obtainStyledAttributes(attrs, R.styleable.ConstraintLayout, 0, 0).apply {
            try {
                viewBinding?.filterName?.text = getString(R.styleable.ConstraintLayout_fieldName)
            } finally {
                recycle()
            }
        }
    }

    fun setupField(field: Field) {
        viewBinding?.filterAccount?.doOnTextChanged { text, _, _, _ ->
            field.value = if (text.isNullOrBlank()) null else text.toString()
        }
        if (field.value != null) {
            viewBinding?.filterAccount?.setText(field.value.toString())
        }
    }

    fun cleanField() {
        viewBinding?.filterAccount?.setText("")
    }

    override fun onViewRemoved(view: View?) {
        super.onViewRemoved(view)
        viewBinding = null
    }
}