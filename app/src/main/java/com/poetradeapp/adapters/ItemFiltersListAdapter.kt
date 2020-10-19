package com.poetradeapp.adapters

import android.content.res.Resources
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.CheckBox
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.poetradeapp.R
import com.poetradeapp.activities.ItemsSearchActivity
import com.poetradeapp.listeners.DropDownChangedListener
import com.poetradeapp.listeners.SocketsLinksTextChangedListener
import com.poetradeapp.models.EnumFilters
import com.poetradeapp.models.Enums
import com.poetradeapp.models.viewmodels.ItemsSearchViewModel
import com.poetradeapp.ui.SlideUpDownAnimator

class ItemFiltersListAdapter(private val items: Array<Enums.Filters>) :
    RecyclerView.Adapter<ItemFiltersListRecyclerViewVH>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemFiltersListRecyclerViewVH {
        val view =
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.filters_main_item, parent, false)

        return ItemFiltersListRecyclerViewVH(view)
    }

    override fun onBindViewHolder(holder: ItemFiltersListRecyclerViewVH, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}

class ItemFiltersListRecyclerViewVH(itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    private val enabled: CheckBox = itemView.findViewById(R.id.filterEnabled)
    private val showHide: MaterialButton = itemView.findViewById(R.id.filterShowHideButton)
    private val filterItemsLayout: FrameLayout = itemView.findViewById(R.id.filterItemsLayout)
    private val context = itemView.context as ItemsSearchActivity
    private val animator = SlideUpDownAnimator(filterItemsLayout)
    private val recyclerView = RecyclerView(context)
    internal val filters by lazy {
        ViewModelProvider(
            context,
            ViewModelProvider.AndroidViewModelFactory(context.application)
        ).get(ItemsSearchViewModel::class.java).getItemRequestData().query.filters
    }

    init {
        showHide.setOnClickListener {
            if (filterItemsLayout.visibility == View.VISIBLE)
                animator.slideUp()
            else
                animator.slideDown()
        }

        val divider = DividerItemDecoration(context, RecyclerView.VERTICAL)
        ContextCompat.getDrawable(context, R.drawable.table_column_divider)
            ?.let { divider.setDrawable(it) }

        recyclerView.layoutParams = RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(divider)
    }

    fun bind(item: Enums.Filters) {
        showHide.text = item.text
        when (item) {
            Enums.Filters.TypeFilter -> {
                enabled.isChecked = !filters.type_filters.disabled
                enabled.setOnCheckedChangeListener { _, checked ->
                    filters.type_filters.disabled = !checked
                }
                val view = LayoutInflater
                    .from(context)
                    .inflate(R.layout.fragment_type_filters, null)
                val itemCategory =
                    view.findViewById<AutoCompleteTextView>(R.id.itemCategoryDropDown)
                val itemRarity =
                    view.findViewById<AutoCompleteTextView>(R.id.itemRarityDropDown)
                itemCategory.setAdapter(
                    DropDownAdapter(
                        itemView.context,
                        R.layout.dropdown_item,
                        R.id.itemText,
                        Enums.ItemCategory.values().toList()
                    )
                )
                itemCategory.setText(Enums.ItemCategory.Any.text, false)
                itemRarity.setAdapter(
                    DropDownAdapter(
                        itemView.context,
                        R.layout.dropdown_item,
                        R.id.itemText,
                        Enums.ItemRarity.values().toList()
                    )
                )
                itemRarity.setText(Enums.ItemRarity.Any.text, false)
                itemCategory.onItemClickListener =
                    DropDownChangedListener(EnumFilters.TypeFilters.Category, filters)
                itemRarity.onItemClickListener =
                    DropDownChangedListener(EnumFilters.TypeFilters.Rarity, filters)
                view.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                filterItemsLayout.addView(view)
            }
            Enums.Filters.WeaponFilter -> {
                enabled.isChecked = !filters.weapon_filters.disabled
                enabled.setOnCheckedChangeListener { _, checked ->
                    filters.weapon_filters.disabled = !checked
                }
                recyclerView.setItemViewCacheSize(6)
                recyclerView.adapter = ItemFilterAdapter(EnumFilters.WeaponFilters.values())
                filterItemsLayout.addView(recyclerView)
            }
            Enums.Filters.ArmourFilter -> {
                enabled.isChecked = !filters.armour_filters.disabled
                enabled.setOnCheckedChangeListener { _, checked ->
                    filters.armour_filters.disabled = !checked
                }
                recyclerView.setItemViewCacheSize(4)
                recyclerView.adapter = ItemFilterAdapter(EnumFilters.ArmourFilters.values())
                filterItemsLayout.addView(recyclerView)
            }
            Enums.Filters.SocketFilter -> {
                enabled.isChecked = !filters.socket_filters.disabled
                enabled.setOnCheckedChangeListener { _, checked ->
                    filters.socket_filters.disabled = !checked
                }
                val view = LayoutInflater
                    .from(context)
                    .inflate(R.layout.fragment_socket_filters, null)
                view.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                view.findViewById<TextInputEditText>(R.id.redSocketSocketsFilter)
                    .addTextChangedListener(
                        SocketsLinksTextChangedListener(
                            EnumFilters.SocketFilters.Sockets,
                            EnumFilters.SocketTypes.R,
                            filters.socket_filters.filters
                        )
                    )
                view.findViewById<TextInputEditText>(R.id.greenSocketSocketsFilter)
                    .addTextChangedListener(
                        SocketsLinksTextChangedListener(
                            EnumFilters.SocketFilters.Sockets,
                            EnumFilters.SocketTypes.G,
                            filters.socket_filters.filters
                        )
                    )
                view.findViewById<TextInputEditText>(R.id.blueSocketSocketsFilter)
                    .addTextChangedListener(
                        SocketsLinksTextChangedListener(
                            EnumFilters.SocketFilters.Sockets,
                            EnumFilters.SocketTypes.B,
                            filters.socket_filters.filters
                        )
                    )
                view.findViewById<TextInputEditText>(R.id.whiteSocketSocketsFilter)
                    .addTextChangedListener(
                        SocketsLinksTextChangedListener(
                            EnumFilters.SocketFilters.Sockets,
                            EnumFilters.SocketTypes.W,
                            filters.socket_filters.filters
                        )
                    )
                view.findViewById<TextInputEditText>(R.id.minSocketSocketsFilter)
                    .addTextChangedListener(
                        SocketsLinksTextChangedListener(
                            EnumFilters.SocketFilters.Sockets,
                            EnumFilters.SocketTypes.MIN,
                            filters.socket_filters.filters
                        )
                    )
                view.findViewById<TextInputEditText>(R.id.maxSocketSocketsFilter)
                    .addTextChangedListener(
                        SocketsLinksTextChangedListener(
                            EnumFilters.SocketFilters.Sockets,
                            EnumFilters.SocketTypes.MAX,
                            filters.socket_filters.filters
                        )
                    )
                view.findViewById<TextInputEditText>(R.id.redSocketLinksFilter)
                    .addTextChangedListener(
                        SocketsLinksTextChangedListener(
                            EnumFilters.SocketFilters.Links,
                            EnumFilters.SocketTypes.R,
                            filters.socket_filters.filters
                        )
                    )
                view.findViewById<TextInputEditText>(R.id.greenSocketLinksFilter)
                    .addTextChangedListener(
                        SocketsLinksTextChangedListener(
                            EnumFilters.SocketFilters.Links,
                            EnumFilters.SocketTypes.G,
                            filters.socket_filters.filters
                        )
                    )
                view.findViewById<TextInputEditText>(R.id.blueSocketLinksFilter)
                    .addTextChangedListener(
                        SocketsLinksTextChangedListener(
                            EnumFilters.SocketFilters.Links,
                            EnumFilters.SocketTypes.B,
                            filters.socket_filters.filters
                        )
                    )
                view.findViewById<TextInputEditText>(R.id.whiteSocketLinksFilter)
                    .addTextChangedListener(
                        SocketsLinksTextChangedListener(
                            EnumFilters.SocketFilters.Links,
                            EnumFilters.SocketTypes.W,
                            filters.socket_filters.filters
                        )
                    )
                view.findViewById<TextInputEditText>(R.id.minSocketLinksFilter)
                    .addTextChangedListener(
                        SocketsLinksTextChangedListener(
                            EnumFilters.SocketFilters.Links,
                            EnumFilters.SocketTypes.MIN,
                            filters.socket_filters.filters
                        )
                    )
                view.findViewById<TextInputEditText>(R.id.maxSocketLinksFilter)
                    .addTextChangedListener(
                        SocketsLinksTextChangedListener(
                            EnumFilters.SocketFilters.Links,
                            EnumFilters.SocketTypes.MAX,
                            filters.socket_filters.filters
                        )
                    )
                filterItemsLayout.addView(view)
            }
            Enums.Filters.ReqFilter -> {
                enabled.isChecked = !filters.req_filters.disabled
                enabled.setOnCheckedChangeListener { _, checked ->
                    filters.req_filters.disabled = !checked
                }
                recyclerView.setItemViewCacheSize(4)
                recyclerView.adapter = ItemFilterAdapter(EnumFilters.ReqFilter.values())
                filterItemsLayout.addView(recyclerView)
            }
            Enums.Filters.MapFilter -> {
                enabled.isChecked = !filters.map_filters.disabled
                enabled.setOnCheckedChangeListener { _, checked ->
                    filters.map_filters.disabled = !checked
                }
                recyclerView.setItemViewCacheSize(9)
                recyclerView.adapter = ItemFilterAdapter(EnumFilters.MapFilter.values())
                filterItemsLayout.addView(recyclerView)
            }
            Enums.Filters.HeistFilter -> {
                enabled.isChecked = !filters.heist_filters.disabled
                recyclerView.setItemViewCacheSize(17)
                recyclerView.adapter = ItemFilterAdapter(EnumFilters.HeistFilter.values())
                filterItemsLayout.addView(recyclerView)
            }
            Enums.Filters.MiscFilter -> {
                enabled.isChecked = !filters.misc_filters.disabled
                enabled.setOnCheckedChangeListener { _, checked ->
                    filters.misc_filters.disabled = !checked
                }
                recyclerView.setItemViewCacheSize(23)
                recyclerView.adapter = ItemFilterAdapter(EnumFilters.MiscFilter.values())
                filterItemsLayout.addView(recyclerView)
            }
            Enums.Filters.TradeFilter -> {
                enabled.isChecked = !filters.trade_filters.disabled
                enabled.setOnCheckedChangeListener { _, checked ->
                    filters.trade_filters.disabled = !checked
                }
                val view = LayoutInflater
                    .from(context)
                    .inflate(R.layout.fragment_trade_filters, null)
                val sellerAccount =
                    view.findViewById<TextInputEditText>(R.id.trade_filters_sellerAccount)
                val listed =
                    view.findViewById<AutoCompleteTextView>(R.id.trade_filters_listed)
                val saleType =
                    view.findViewById<AutoCompleteTextView>(R.id.trade_filters_saleType)
                val buyoutPrice =
                    view.findViewById<AutoCompleteTextView>(R.id.trade_filters_buyoutPrice)
                val buyoutPriceMin =
                    view.findViewById<TextInputEditText>(R.id.trade_filters_buyoutPriceMin)
                val buyoutPriceMax =
                    view.findViewById<TextInputEditText>(R.id.trade_filters_buyoutPriceMax)

                sellerAccount.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) =
                        Unit

                    override fun afterTextChanged(p0: Editable?) = Unit

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        filters.trade_filters.filters.account = p0?.toString()
                    }
                })

                buyoutPriceMin.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) =
                        Unit

                    override fun afterTextChanged(p0: Editable?) = Unit

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                        filters.trade_filters.filters.price.min = p0?.toString()?.toIntOrNull()
                    }
                })

                buyoutPriceMax.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) =
                        Unit

                    override fun afterTextChanged(p0: Editable?) = Unit

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                        filters.trade_filters.filters.price.max = p0?.toString()?.toIntOrNull()
                    }
                })

                listed.setAdapter(
                    DropDownAdapter(
                        itemView.context,
                        R.layout.dropdown_item,
                        R.id.itemText,
                        Enums.Listed.values().toList()
                    )
                )
                listed.setText(Enums.Listed.Any.text, false)
                listed.onItemClickListener =
                    DropDownChangedListener(EnumFilters.TradeFilters.Listed, filters)

                saleType.setAdapter(
                    DropDownAdapter(
                        itemView.context,
                        R.layout.dropdown_item,
                        R.id.itemText,
                        Enums.SaleType.values().toList()
                    )
                )
                saleType.setText(Enums.SaleType.Any.text, false)
                saleType.onItemClickListener =
                    DropDownChangedListener(EnumFilters.TradeFilters.SaleType, filters)

                buyoutPrice.setAdapter(
                    DropDownAdapter(
                        itemView.context,
                        R.layout.dropdown_item,
                        R.id.itemText,
                        Enums.BuyoutPrice.values().toList()
                    )
                )
                buyoutPrice.setText(Enums.BuyoutPrice.ChaosEquivalent.text, false)
                buyoutPrice.onItemClickListener =
                    DropDownChangedListener(EnumFilters.TradeFilters.BuyoutPrice, filters)

                view.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                filterItemsLayout.addView(view)
            }
        }

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