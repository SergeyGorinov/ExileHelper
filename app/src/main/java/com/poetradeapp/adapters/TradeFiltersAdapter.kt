package com.poetradeapp.adapters

import android.content.res.Resources
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.poetradeapp.R
import com.poetradeapp.activities.ItemsSearchActivity
import com.poetradeapp.listeners.DropDownChangedListener
import com.poetradeapp.listeners.EnableDisableFilterListener
import com.poetradeapp.models.enums.Dropdowns
import com.poetradeapp.models.enums.ViewFilters
import com.poetradeapp.models.viewmodels.ItemsSearchViewModel
import com.poetradeapp.ui.SlideUpDownAnimator
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class TradeFiltersAdapter : RecyclerView.Adapter<TradeFiltersViewHolder>() {

    override fun getItemCount() = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TradeFiltersViewHolder {
        val view =
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.filters_trade_item, parent, false)

        return TradeFiltersViewHolder(view)
    }

    override fun onBindViewHolder(holder: TradeFiltersViewHolder, position: Int) = Unit
}

@ExperimentalCoroutinesApi
class TradeFiltersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val enabled: CheckBox = itemView.findViewById(R.id.filterEnabled)
    private val showHide: MaterialButton = itemView.findViewById(R.id.filterShowHideButton)
    private val filterItemsLayout: LinearLayout = itemView.findViewById(R.id.filterItemsLayout)
    private val clearAll: ImageButton = itemView.findViewById(R.id.filterClearAll)
    private val sellerAccount =
        itemView.findViewById<TextInputEditText>(R.id.trade_filters_sellerAccount)
    private val listed =
        itemView.findViewById<AutoCompleteTextView>(R.id.trade_filters_listed)
    private val saleType =
        itemView.findViewById<AutoCompleteTextView>(R.id.trade_filters_saleType)
    private val buyoutPrice =
        itemView.findViewById<AutoCompleteTextView>(R.id.trade_filters_buyoutPrice)
    private val buyoutPriceMin =
        itemView.findViewById<TextInputEditText>(R.id.trade_filters_buyoutPriceMin)
    private val buyoutPriceMax =
        itemView.findViewById<TextInputEditText>(R.id.trade_filters_buyoutPriceMax)
    private val context = itemView.context as ItemsSearchActivity
    private val animator = SlideUpDownAnimator(filterItemsLayout)
    private val filters by lazy {
        ViewModelProvider(
            context,
            ViewModelProvider.AndroidViewModelFactory(context.application)
        ).get(ItemsSearchViewModel::class.java).getItemRequestData().query.filters
    }

    init {
        val filter = filters.trade_filters

        enabled.isChecked = !filter.disabled
        enabled.setOnCheckedChangeListener(EnableDisableFilterListener(filter))

        showHide.setOnClickListener {
            if (filterItemsLayout.visibility == View.VISIBLE)
                animator.slideUp()
            else
                animator.slideDown()
        }

        showHide.text = context.getString(R.string.trade_filter_header)

        sellerAccount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) =
                Unit

            override fun afterTextChanged(p0: Editable?) = Unit

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                filter.filters.account = p0?.toString()
            }
        })

        buyoutPriceMin.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) =
                Unit

            override fun afterTextChanged(p0: Editable?) = Unit

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                filter.filters.price.min = p0?.toString()?.toIntOrNull()
            }
        })

        buyoutPriceMax.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) =
                Unit

            override fun afterTextChanged(p0: Editable?) = Unit

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                filter.filters.price.max = p0?.toString()?.toIntOrNull()
            }
        })

        listed.setAdapter(
            DropDownAdapter(
                itemView.context,
                R.layout.dropdown_item,
                R.id.itemText,
                Dropdowns.Listed.values().toList()
            )
        )
        listed.setText(Dropdowns.Listed.Any.text, false)
        listed.onItemClickListener =
            DropDownChangedListener(ViewFilters.TradeFilters.Listed, filters)

        saleType.setAdapter(
            DropDownAdapter(
                itemView.context,
                R.layout.dropdown_item,
                R.id.itemText,
                Dropdowns.SaleType.values().toList()
            )
        )
        saleType.setText(Dropdowns.SaleType.Any.text, false)
        saleType.onItemClickListener =
            DropDownChangedListener(ViewFilters.TradeFilters.SaleType, filters)

        buyoutPrice.setAdapter(
            DropDownAdapter(
                itemView.context,
                R.layout.dropdown_item,
                R.id.itemText,
                Dropdowns.BuyoutPrice.values().toList()
            )
        )
        buyoutPrice.setText(Dropdowns.BuyoutPrice.ChaosEquivalent.text, false)
        buyoutPrice.onItemClickListener =
            DropDownChangedListener(ViewFilters.TradeFilters.BuyoutPrice, filters)

        val layoutParams = filterItemsLayout.layoutParams
        layoutParams.height = 1
        filterItemsLayout.layoutParams = layoutParams

        filterItemsLayout.measure(
            View.MeasureSpec.makeMeasureSpec(
                Resources.getSystem().displayMetrics.widthPixels,
                View.MeasureSpec.EXACTLY
            ),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )

        animator.setHeight(filterItemsLayout.measuredHeight)
    }
}