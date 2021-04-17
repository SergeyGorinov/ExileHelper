package com.sgorinov.exilehelper.exchange.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.doOnTextChanged
import com.sgorinov.exilehelper.exchange.R
import com.sgorinov.exilehelper.exchange.data.models.Field
import com.sgorinov.exilehelper.exchange.data.models.ItemsRequestModelFields
import com.sgorinov.exilehelper.exchange.databinding.MinmaxViewBinding

internal class MinMaxView(ctx: Context, attrs: AttributeSet) :
    ConstraintLayout(ctx, attrs) {

    var viewBinding: MinmaxViewBinding?

    init {
        val view = LayoutInflater.from(ctx).inflate(R.layout.minmax_view, this, true)
        viewBinding = MinmaxViewBinding.bind(view)

        context.theme.obtainStyledAttributes(attrs, R.styleable.ConstraintLayout, 0, 0).apply {
            try {
                viewBinding?.filterName?.text = getString(R.styleable.ConstraintLayout_fieldName)
            } finally {
                recycle()
            }
        }
    }

    fun setupMinMax(field: Field) {
        viewBinding?.let {
            it.filterMin.doOnTextChanged { _, _, _, _ ->
                val min = it.filterMin.text?.toString()?.toIntOrNull()
                val max = it.filterMax.text?.toString()?.toIntOrNull()
                val value = ItemsRequestModelFields.MinMax(min, max)
                field.value = if (value.isEmpty()) null else value
            }
            it.filterMax.doOnTextChanged { _, _, _, _ ->
                val min = it.filterMin.text?.toString()?.toIntOrNull()
                val max = it.filterMax.text?.toString()?.toIntOrNull()
                val value = ItemsRequestModelFields.MinMax(min, max)
                field.value = if (value.isEmpty()) null else value
            }
            if (field.value != null) {
                val value = field.value as ItemsRequestModelFields.MinMax
                if (value.min != null) {
                    it.filterMin.setText(value.min.toString())
                }
                if (value.max != null) {
                    it.filterMax.setText(value.max.toString())
                }
            }
        }
    }

    fun cleanField() {
        viewBinding?.let {
            it.filterMax.setText("")
            it.filterMin.setText("")
        }
    }

    override fun onViewRemoved(view: View?) {
        super.onViewRemoved(view)
        viewBinding = null
    }
}