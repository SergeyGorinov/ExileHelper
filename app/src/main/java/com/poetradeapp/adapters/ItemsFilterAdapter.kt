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
import com.poetradeapp.models.enums.Dropdowns
import com.poetradeapp.models.enums.IFilter
import com.poetradeapp.models.enums.ViewFilters
import com.poetradeapp.models.enums.getValuesByType
import com.poetradeapp.models.viewmodels.ItemsSearchViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class ItemFilterAdapter(item: ViewFilters.AllFilters) :
    RecyclerView.Adapter<ItemFilterRecyclerViewVH>() {

    private val items = item.getValuesByType()

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
        holder.bind(items[position] as IFilter)
    }

    override fun getItemCount() = items.size
}

@ExperimentalCoroutinesApi
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

    fun bind(item: IFilter) {
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
            filterMin.setText("")
            filterMax.setText("")
        }
    }

    private fun setItems(item: IFilter) {
        if (item is ViewFilters.MapFilters) {
            when (item) {
                ViewFilters.MapFilters.ShapedMap,
                ViewFilters.MapFilters.ElderMap,
                ViewFilters.MapFilters.BlightedMap -> {
                    filterDropDown.setAdapter(
                        DropDownAdapter(
                            itemView.context,
                            R.layout.dropdown_item,
                            R.id.itemText,
                            Dropdowns.YesNo.values().toList()
                        )
                    )
                    filterDropDown.setText(Dropdowns.YesNo.Any.text, false)
                }
                ViewFilters.MapFilters.MapRegion -> {
                    filterDropDown.setAdapter(
                        DropDownAdapter(
                            itemView.context,
                            R.layout.dropdown_item,
                            R.id.itemText,
                            Dropdowns.MapRegion.values().toList()
                        )
                    )
                    filterDropDown.setText(Dropdowns.MapRegion.Any.text, false)
                }
                ViewFilters.MapFilters.MapSeries -> {
                    filterDropDown.setAdapter(
                        DropDownAdapter(
                            itemView.context,
                            R.layout.dropdown_item,
                            R.id.itemText,
                            Dropdowns.MapSeries.values().toList()
                        )
                    )
                    filterDropDown.setText(Dropdowns.MapSeries.Any.text, false)
                }
            }
        }
        if (item is ViewFilters.MiscFilters) {
            when (item) {
                ViewFilters.MiscFilters.GemQualityType -> {
                    filterDropDown.setAdapter(
                        DropDownAdapter(
                            itemView.context,
                            R.layout.dropdown_item,
                            R.id.itemText,
                            Dropdowns.GemQualityType.values().toList()
                        )
                    )
                    filterDropDown.setText(Dropdowns.GemQualityType.Any.text, false)
                }
                ViewFilters.MiscFilters.ShaperInfluence,
                ViewFilters.MiscFilters.ElderInfluence,
                ViewFilters.MiscFilters.CrusaderInfluence,
                ViewFilters.MiscFilters.RedeemerInfluence,
                ViewFilters.MiscFilters.HunterInfluence,
                ViewFilters.MiscFilters.WarlordInfluence,
                ViewFilters.MiscFilters.FracturedItem,
                ViewFilters.MiscFilters.SynthesisedItem,
                ViewFilters.MiscFilters.AlternateArt,
                ViewFilters.MiscFilters.Identified,
                ViewFilters.MiscFilters.Corrupted,
                ViewFilters.MiscFilters.Mirrored,
                ViewFilters.MiscFilters.Crafted,
                ViewFilters.MiscFilters.Veiled,
                ViewFilters.MiscFilters.Enchanted -> {
                    filterDropDown.setAdapter(
                        DropDownAdapter(
                            itemView.context,
                            R.layout.dropdown_item,
                            R.id.itemText,
                            Dropdowns.YesNo.values().toList()
                        )
                    )
                    filterDropDown.setText(Dropdowns.YesNo.Any.text, false)
                }
            }
        }
        if (item is ViewFilters.HeistFilters) {
            when (item) {
                ViewFilters.HeistFilters.ContractObjectiveValue -> {
                    filterDropDown.setAdapter(
                        DropDownAdapter(
                            itemView.context,
                            R.layout.dropdown_item,
                            R.id.itemText,
                            Dropdowns.ContractObjectiveValue.values().toList()
                        )
                    )
                    filterDropDown.setText(Dropdowns.ContractObjectiveValue.Any.text, false)
                }
            }
        }
    }
}