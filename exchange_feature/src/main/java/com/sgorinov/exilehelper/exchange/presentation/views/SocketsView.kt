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
import com.sgorinov.exilehelper.exchange.databinding.SocketsViewBinding

internal class SocketsView(ctx: Context, attrs: AttributeSet) : ConstraintLayout(ctx, attrs) {

    var viewBinding: SocketsViewBinding?

    init {
        val view = LayoutInflater.from(ctx).inflate(R.layout.sockets_view, this, true)
        viewBinding = SocketsViewBinding.bind(view)

        context.theme.obtainStyledAttributes(attrs, R.styleable.ConstraintLayout, 0, 0).apply {
            try {
                viewBinding?.filterName?.text = getString(R.styleable.ConstraintLayout_fieldName)
            } finally {
                recycle()
            }
        }
    }

    fun setupField(field: Field) {
        viewBinding?.let {
            it.filterRed.doOnTextChanged { _, _, _, _ ->
                field.value = getSocketGroupData()
            }
            it.filterGreen.doOnTextChanged { _, _, _, _ ->
                field.value = getSocketGroupData()
            }
            it.filterBlue.doOnTextChanged { _, _, _, _ ->
                field.value = getSocketGroupData()
            }
            it.filterWhite.doOnTextChanged { _, _, _, _ ->
                field.value = getSocketGroupData()
            }
            it.filterMin.doOnTextChanged { _, _, _, _ ->
                field.value = getSocketGroupData()
            }
            it.filterMax.doOnTextChanged { _, _, _, _ ->
                field.value = getSocketGroupData()
            }
        }
    }

    fun cleanField() {
        viewBinding?.let {
            it.filterRed.setText("")
            it.filterGreen.setText("")
            it.filterBlue.setText("")
            it.filterWhite.setText("")
            it.filterMin.setText("")
            it.filterMax.setText("")
        }
    }

    override fun onViewRemoved(view: View?) {
        super.onViewRemoved(view)
        viewBinding = null
    }

    private fun getSocketGroupData(): ItemsRequestModelFields.Sockets? {
        val value = ItemsRequestModelFields.Sockets(
            viewBinding?.filterRed?.text?.toString()?.toIntOrNull(),
            viewBinding?.filterGreen?.text?.toString()?.toIntOrNull(),
            viewBinding?.filterBlue?.text?.toString()?.toIntOrNull(),
            viewBinding?.filterWhite?.text?.toString()?.toIntOrNull(),
            viewBinding?.filterMin?.text?.toString()?.toIntOrNull(),
            viewBinding?.filterMax?.text?.toString()?.toIntOrNull()
        )
        return if (value.isEmpty())
            null
        else
            value
    }

}