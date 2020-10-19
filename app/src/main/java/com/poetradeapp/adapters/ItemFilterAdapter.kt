package com.poetradeapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.GridLayout
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.poetradeapp.R
import com.poetradeapp.activities.ItemsSearchActivity
import com.poetradeapp.listeners.DropDownChangedListener
import com.poetradeapp.listeners.MinMaxTextChangedListener
import com.poetradeapp.models.EnumFilters
import com.poetradeapp.models.Enums
import com.poetradeapp.models.FiltersEnum
import com.poetradeapp.models.viewmodels.ItemsSearchViewModel

class ItemFilterAdapter<T>(private val items: Array<T>) :
    RecyclerView.Adapter<ItemFilterRecyclerViewVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemFilterRecyclerViewVH {
        val activity = parent.context as ItemsSearchActivity

        val view = LayoutInflater
            .from(activity)
            .inflate(R.layout.filters_dropdown_item, parent, false)

        val model = ViewModelProvider(
            activity,
            ViewModelProvider.AndroidViewModelFactory(activity.application)
        ).get(ItemsSearchViewModel::class.java)

        return ItemFilterRecyclerViewVH(view, model)
    }

    override fun onBindViewHolder(holder: ItemFilterRecyclerViewVH, position: Int) {
        holder.bind(items[position] as FiltersEnum)
    }

    override fun getItemCount() = items.size
}

class ItemFilterRecyclerViewVH(
    itemView: View,
    model: ItemsSearchViewModel
) :
    RecyclerView.ViewHolder(itemView) {
    private val filterName: TextView = itemView.findViewById(R.id.filterName)
    private val filterMinMax: GridLayout = itemView.findViewById(R.id.minMaxLayout)
    private val filterMin: TextInputEditText = filterMinMax.findViewById(R.id.filterMin)
    private val filterMax: TextInputEditText = filterMinMax.findViewById(R.id.filterMax)
    private val filterDropDownLayout: TextInputLayout = itemView.findViewById(R.id.dropDownLayout)
    private val filterDropDown: AutoCompleteTextView =
        filterDropDownLayout.findViewById(R.id.filterDropDown)
    private val filters = model.getItemRequestData().query.filters

    fun bind(item: FiltersEnum) {
        filterName.text = item.text
        if (item.isDropDown) {
            filterMinMax.visibility = View.GONE
            filterDropDownLayout.visibility = View.VISIBLE
            filterDropDown.onItemClickListener = DropDownChangedListener(item, filters)
            setItems(item)
        } else {
            filterMin.addTextChangedListener(
                MinMaxTextChangedListener(
                    item,
                    filterMin,
                    filterMax,
                    filters
                )
            )
            filterMax.addTextChangedListener(
                MinMaxTextChangedListener(
                    item,
                    filterMin,
                    filterMax,
                    filters
                )
            )
        }
    }

    private fun setItems(item: FiltersEnum) {
        if (item is EnumFilters.MapFilter) {
            when (item) {
                EnumFilters.MapFilter.ShapedMap,
                EnumFilters.MapFilter.ElderMap,
                EnumFilters.MapFilter.BlightedMap -> {
                    filterDropDown.setAdapter(
                        DropDownAdapter(
                            itemView.context,
                            R.layout.dropdown_item,
                            R.id.itemText,
                            Enums.YesNo.values().toList()
                        )
                    )
                    filterDropDown.setText(Enums.YesNo.Any.text, false)
                }
                EnumFilters.MapFilter.MapRegion -> {
                    filterDropDown.setAdapter(
                        DropDownAdapter(
                            itemView.context,
                            R.layout.dropdown_item,
                            R.id.itemText,
                            Enums.MapRegion.values().toList()
                        )
                    )
                    filterDropDown.setText(Enums.MapRegion.Any.text, false)
                }
                EnumFilters.MapFilter.MapSeries -> {
                    filterDropDown.setAdapter(
                        DropDownAdapter(
                            itemView.context,
                            R.layout.dropdown_item,
                            R.id.itemText,
                            Enums.MapSeries.values().toList()
                        )
                    )
                    filterDropDown.setText(Enums.MapSeries.Any.text, false)
                }
            }
        }
        if (item is EnumFilters.MiscFilter) {
            when (item) {
                EnumFilters.MiscFilter.GemQualityType -> {
                    filterDropDown.setAdapter(
                        DropDownAdapter(
                            itemView.context,
                            R.layout.dropdown_item,
                            R.id.itemText,
                            Enums.GemQualityType.values().toList()
                        )
                    )
                    filterDropDown.setText(Enums.GemQualityType.Any.text, false)
                }
                EnumFilters.MiscFilter.ShaperInfluence,
                EnumFilters.MiscFilter.ElderInfluence,
                EnumFilters.MiscFilter.CrusaderInfluence,
                EnumFilters.MiscFilter.RedeemerInfluence,
                EnumFilters.MiscFilter.HunterInfluence,
                EnumFilters.MiscFilter.WarlordInfluence,
                EnumFilters.MiscFilter.FracturedItem,
                EnumFilters.MiscFilter.SynthesisedItem,
                EnumFilters.MiscFilter.AlternateArt,
                EnumFilters.MiscFilter.Identified,
                EnumFilters.MiscFilter.Corrupted,
                EnumFilters.MiscFilter.Mirrored,
                EnumFilters.MiscFilter.Crafted,
                EnumFilters.MiscFilter.Veiled,
                EnumFilters.MiscFilter.Enchanted -> {
                    filterDropDown.setAdapter(
                        DropDownAdapter(
                            itemView.context,
                            R.layout.dropdown_item,
                            R.id.itemText,
                            Enums.YesNo.values().toList()
                        )
                    )
                    filterDropDown.setText(Enums.YesNo.Any.text, false)
                }
            }
        }
        if (item is EnumFilters.HeistFilter) {
            when (item) {
                EnumFilters.HeistFilter.ContractObjectiveValue -> {
                    filterDropDown.setAdapter(
                        DropDownAdapter(
                            itemView.context,
                            R.layout.dropdown_item,
                            R.id.itemText,
                            Enums.ContractObjectiveValue.values().toList()
                        )
                    )
                    filterDropDown.setText(Enums.ContractObjectiveValue.Any.text, false)
                }
            }
        }
    }
}