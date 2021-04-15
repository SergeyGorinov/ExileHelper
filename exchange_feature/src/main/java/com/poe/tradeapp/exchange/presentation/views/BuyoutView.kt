package com.poe.tradeapp.exchange.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.AutoCompleteTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import com.poe.tradeapp.exchange.R
import com.poe.tradeapp.exchange.data.models.Field
import com.poe.tradeapp.exchange.data.models.ItemsRequestModelFields
import com.poe.tradeapp.exchange.databinding.BuyoutViewBinding
import com.poe.tradeapp.exchange.presentation.adapters.DropDownAdapter
import com.poe.tradeapp.exchange.presentation.models.enums.IEnum

internal class BuyoutView(private val ctx: Context, attrs: AttributeSet) :
    ConstraintLayout(ctx, attrs) {

    val viewBinding: BuyoutViewBinding

    private var selectedItem: IEnum? = null

    init {
        val view = LayoutInflater.from(ctx).inflate(R.layout.buyout_view, this, true)
        viewBinding = BuyoutViewBinding.bind(view)
    }

    fun setupDropDown(
        field: Field,
        dropDownValues: List<Any?>
    ) {
        viewBinding.filterDropDown.setAdapter(
            DropDownAdapter(
                ctx,
                R.layout.dropdown_item,
                dropDownValues
            )
        )
        viewBinding.filterDropDown.setText((dropDownValues.first() as? IEnum)?.text, false)
        viewBinding.filterDropDown.setOnFocusChangeListener { autoCompleteTextView, focused ->
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
        viewBinding.filterDropDown.setOnItemClickListener { adapterView, _, position, _ ->
            selectedItem =
                adapterView.getItemAtPosition(position) as? IEnum ?: return@setOnItemClickListener
            val adapter = adapterView.adapter
            val min = viewBinding.minValue.text?.toString()?.toIntOrNull()
            val max = viewBinding.maxValue.text?.toString()?.toIntOrNull()
            val value = ItemsRequestModelFields.Price(min, max, selectedItem?.id)
            field.value = if (value.isEmpty()) null else value
            if (adapter is DropDownAdapter) {
                adapter.selectedItem = selectedItem
            }
        }
        viewBinding.filterDropDown.typeface = ResourcesCompat.getFont(ctx, R.font.fontinsmallcaps)
    }
}