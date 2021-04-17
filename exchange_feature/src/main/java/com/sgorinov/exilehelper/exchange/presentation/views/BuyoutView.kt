package com.sgorinov.exilehelper.exchange.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.AutoCompleteTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import com.sgorinov.exilehelper.exchange.R
import com.sgorinov.exilehelper.exchange.data.models.Field
import com.sgorinov.exilehelper.exchange.data.models.ItemsRequestModelFields
import com.sgorinov.exilehelper.exchange.databinding.BuyoutViewBinding
import com.sgorinov.exilehelper.exchange.presentation.adapters.DropDownAdapter
import com.sgorinov.exilehelper.exchange.presentation.models.enums.IEnum

internal class BuyoutView(private val ctx: Context, attrs: AttributeSet) :
    ConstraintLayout(ctx, attrs) {

    var viewBinding: BuyoutViewBinding?

    private var selectedItem: IEnum? = null

    init {
        val view = LayoutInflater.from(ctx).inflate(R.layout.buyout_view, this, true)
        viewBinding = BuyoutViewBinding.bind(view)

        context.theme.obtainStyledAttributes(attrs, R.styleable.ConstraintLayout, 0, 0).apply {
            try {
                viewBinding?.filterName?.text = getString(R.styleable.ConstraintLayout_fieldName)
            } finally {
                recycle()
            }
        }
    }

    fun setupDropDown(
        field: Field,
        dropDownValues: List<Any?>
    ) {
        viewBinding?.let {
            it.filterDropDown.setAdapter(
                DropDownAdapter(
                    ctx,
                    R.layout.dropdown_item,
                    dropDownValues
                )
            )
            it.filterDropDown.setText((dropDownValues.first() as? IEnum)?.text, false)
            it.filterDropDown.setOnFocusChangeListener { autoCompleteTextView, focused ->
                val adapter = (autoCompleteTextView as AutoCompleteTextView).adapter
                if (adapter is DropDownAdapter) {
                    if (focused) {
                        autoCompleteTextView.hint = adapter.selectedItem?.text
                        autoCompleteTextView.setText("", false)
                    } else {
                        autoCompleteTextView.setText(adapter.selectedItem?.text, false)
                    }
                }
            }
            it.filterDropDown.setOnItemClickListener { adapterView, _, position, _ ->
                selectedItem =
                    adapterView.getItemAtPosition(position) as? IEnum
                        ?: return@setOnItemClickListener
                val adapter = adapterView.adapter
                val min = it.minValue.text?.toString()?.toIntOrNull()
                val max = it.maxValue.text?.toString()?.toIntOrNull()
                val value = ItemsRequestModelFields.Price(min, max, selectedItem?.id)
                field.value = if (value.isEmpty()) null else value
                if (adapter is DropDownAdapter) {
                    adapter.selectedItem = selectedItem
                }
            }
            it.filterDropDown.typeface = ResourcesCompat.getFont(ctx, R.font.fontinsmallcaps)
        }
    }

    fun cleanField() {
        val adapter = viewBinding?.filterDropDown?.adapter as? DropDownAdapter
        val firstValue = adapter?.itemsAll?.firstOrNull() as? IEnum
        adapter?.selectedItem = firstValue
        viewBinding?.filterDropDown?.setText(firstValue?.text ?: "", false)
        viewBinding?.minValue?.setText("")
        viewBinding?.maxValue?.setText("")
    }

    override fun onViewRemoved(view: View?) {
        super.onViewRemoved(view)
        viewBinding = null
    }
}