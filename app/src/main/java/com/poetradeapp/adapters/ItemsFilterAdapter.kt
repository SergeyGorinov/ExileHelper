package com.poetradeapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.poetradeapp.R
import com.poetradeapp.models.enums.IEnum
import com.poetradeapp.models.enums.IFilter
import com.poetradeapp.models.enums.ViewType
import com.poetradeapp.models.request.ItemsRequestModelFields
import com.poetradeapp.models.ui.Filter
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class ItemFilterAdapter(private val items: Array<*>, private val filter: Filter) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        getViewHolderByType(viewType, LayoutInflater.from(parent.context), parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is IBindableFieldViewHolder) {
            holder.bind(items[position] as IFilter, filter)
        }
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int) =
        (items[position] as IFilter).viewType.ordinal

    override fun getItemId(position: Int) = items[position].hashCode().toLong()

    private fun getViewHolderByType(
        viewType: Int,
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            ViewType.Dropdown.ordinal -> {
                val view = layoutInflater.inflate(
                    R.layout.filters_dropdown_item,
                    parent,
                    false
                )
                DropDownViewHolder(view)
            }
            ViewType.Minmax.ordinal -> {
                val view = layoutInflater.inflate(
                    R.layout.filters_minmax_item,
                    parent,
                    false
                )
                MinMaxViewHolder(view)
            }
            ViewType.Socket.ordinal -> {
                val view = layoutInflater.inflate(
                    R.layout.filters_socket_item,
                    parent,
                    false
                )
                SocketViewHolder(view)
            }
            ViewType.Account.ordinal -> {
                val view = layoutInflater.inflate(
                    R.layout.filters_account_item,
                    parent,
                    false
                )
                AccountViewHolder(view)
            }
            ViewType.Buyout.ordinal -> {
                val view = layoutInflater.inflate(
                    R.layout.filters_buyout_item,
                    parent,
                    false
                )
                BuyoutViewHolder(view)
            }
            else -> {
                val view = layoutInflater.inflate(
                    R.layout.filters_dropdown_item,
                    parent,
                    false
                )
                DropDownViewHolder(view)
            }
        }
    }
}

@ExperimentalCoroutinesApi
class DropDownViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    IBindableFieldViewHolder {
    private val filterName: TextView = itemView.findViewById(R.id.filterName)
    private val filterDropDown: AutoCompleteTextView = itemView.findViewById(R.id.filterDropDown)

    override fun bind(item: IFilter, filter: Filter) {
        val field = filter.getField(item.id ?: "")

        filterName.text = item.text

        filterDropDown.setAdapter(
            DropDownAdapter(
                itemView.context,
                R.layout.dropdown_item,
                item.dropDownValues?.toList() ?: listOf()
            )
        )

        filterDropDown.setText((item.dropDownValues?.first() as IEnum?)?.text, false)

        filterDropDown.setOnItemClickListener { adapterView, _, position, _ ->
            val value = adapterView.getItemAtPosition(position) as IEnum?
            val adapter = adapterView.adapter
            field.value =
                if (value?.id != null) ItemsRequestModelFields.DropDown(value.id) else null
            if (adapter is DropDownAdapter)
                adapter.selectedItem = value
        }

        filterDropDown.setOnFocusChangeListener { view, focused ->
            val adapter = (view as AutoCompleteTextView).adapter
            if (adapter is DropDownAdapter) {
                if (focused) {
                    view.hint = adapter.selectedItem?.text
                    view.setText("", false)
                } else {
                    view.setText(adapter.selectedItem?.text, false)
                }
            }
        }

        if (field.value != null) {
            val currentValue = field.value as ItemsRequestModelFields.DropDown
            val value =
                (item.dropDownValues?.singleOrNull { s -> (s as IEnum).id == currentValue.option }) as IEnum
            filterDropDown.setText(value.text, false)
        }
    }
}

@ExperimentalCoroutinesApi
class MinMaxViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    IBindableFieldViewHolder {
    private val filterName: TextView = itemView.findViewById(R.id.filterName)
    private val filterMin: TextInputEditText = itemView.findViewById(R.id.filterMin)
    private val filterMax: TextInputEditText = itemView.findViewById(R.id.filterMax)

    override fun bind(item: IFilter, filter: Filter) {
        val field = filter.getField(item.id ?: "")

        filterName.text = item.text

        filterMin.doOnTextChanged { _, _, _, _ ->
            val min = filterMin.text?.toString()?.toIntOrNull()
            val max = filterMax.text?.toString()?.toIntOrNull()
            val value = ItemsRequestModelFields.MinMax(min, max)
            field.value = if (value.isEmpty()) null else value
        }

        filterMax.doOnTextChanged { _, _, _, _ ->
            val min = filterMin.text?.toString()?.toIntOrNull()
            val max = filterMax.text?.toString()?.toIntOrNull()
            val value = ItemsRequestModelFields.MinMax(min, max)
            field.value = if (value.isEmpty()) null else value
        }

        if (field.value != null) {
            val value = field.value as ItemsRequestModelFields.MinMax
            if (value.min != null) {
                filterMin.setText(value.min.toString())
            }
            if (value.max != null) {
                filterMax.setText(value.max.toString())
            }
        }
    }
}

@ExperimentalCoroutinesApi
class SocketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    IBindableFieldViewHolder {
    private val filterName: TextView = itemView.findViewById(R.id.filterName)
    private val filterRed: TextInputEditText = itemView.findViewById(R.id.filterRed)
    private val filterGreen: TextInputEditText = itemView.findViewById(R.id.filterGreen)
    private val filterBlue: TextInputEditText = itemView.findViewById(R.id.filterBlue)
    private val filterWhite: TextInputEditText = itemView.findViewById(R.id.filterWhite)
    private val filterMin: TextInputEditText = itemView.findViewById(R.id.filterMin)
    private val filterMax: TextInputEditText = itemView.findViewById(R.id.filterMax)

    override fun bind(item: IFilter, filter: Filter) {
        val field = filter.getField(item.id ?: "")
        filterName.text = item.text

        filterRed.doOnTextChanged { _, _, _, _ ->
            field.value = getSocketGroupData()
        }
        filterGreen.doOnTextChanged { _, _, _, _ ->
            field.value = getSocketGroupData()
        }
        filterBlue.doOnTextChanged { _, _, _, _ ->
            field.value = getSocketGroupData()
        }
        filterWhite.doOnTextChanged { _, _, _, _ ->
            field.value = getSocketGroupData()
        }
        filterMin.doOnTextChanged { _, _, _, _ ->
            field.value = getSocketGroupData()
        }
        filterMax.doOnTextChanged { _, _, _, _ ->
            field.value = getSocketGroupData()
        }
    }

    private fun getSocketGroupData(): ItemsRequestModelFields.Sockets? {
        val value = ItemsRequestModelFields.Sockets(
            filterRed.text?.toString()?.toIntOrNull(),
            filterGreen.text?.toString()?.toIntOrNull(),
            filterBlue.text?.toString()?.toIntOrNull(),
            filterWhite.text?.toString()?.toIntOrNull(),
            filterMin.text?.toString()?.toIntOrNull(),
            filterMax.text?.toString()?.toIntOrNull()
        )
        return if (value.isEmpty())
            null
        else
            value
    }
}

@ExperimentalCoroutinesApi
class AccountViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    IBindableFieldViewHolder {
    private val filterName: TextView = itemView.findViewById(R.id.filterName)
    private val filterAccount: TextInputEditText = itemView.findViewById(R.id.filterAccount)

    override fun bind(item: IFilter, filter: Filter) {
        val field = filter.getField(item.id ?: "")
        filterName.text = item.text
        filterAccount.doOnTextChanged { text, _, _, _ ->
            field.value = if (text.isNullOrBlank()) null else text.toString()
        }
        if (field.value != null)
            filterAccount.setText(field.value.toString())
    }
}

@ExperimentalCoroutinesApi
class BuyoutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    IBindableFieldViewHolder {
    private val filterName: TextView = itemView.findViewById(R.id.filterName)
    private val filterDropDown: AutoCompleteTextView = itemView.findViewById(R.id.filterDropDown)
    private val minValue: TextInputEditText = itemView.findViewById(R.id.minValue)
    private val maxValue: TextInputEditText = itemView.findViewById(R.id.maxValue)

    private var selectedItem: IEnum? = null

    override fun bind(item: IFilter, filter: Filter) {
        val field = filter.getField(item.id ?: "")

        filterName.text = item.text

        filterDropDown.setAdapter(
            DropDownAdapter(
                itemView.context,
                R.layout.dropdown_item,
                item.dropDownValues?.toList() ?: listOf()
            )
        )

        filterDropDown.setText((item.dropDownValues?.first() as IEnum?)?.text, false)

        filterDropDown.setOnFocusChangeListener { view, focused ->
            val adapter = (view as AutoCompleteTextView).adapter
            if (adapter is DropDownAdapter) {
                if (focused) {
                    view.hint = adapter.selectedItem?.text
                    view.setText("", false)
                } else {
                    view.setText(adapter.selectedItem?.text, false)
                }
            }
        }

        filterDropDown.setOnItemClickListener { adapterView, _, position, _ ->
            selectedItem = adapterView.getItemAtPosition(position) as IEnum?
            val adapter = adapterView.adapter
            val min = minValue.text?.toString()?.toIntOrNull()
            val max = maxValue.text?.toString()?.toIntOrNull()
            val value = ItemsRequestModelFields.Price(min, max, selectedItem?.id)
            field.value = if (value.isEmpty()) null else value
            if (adapter is DropDownAdapter)
                adapter.selectedItem = selectedItem
        }

        minValue.doOnTextChanged { _, _, _, _ ->
            val min = minValue.text?.toString()?.toIntOrNull()
            val max = maxValue.text?.toString()?.toIntOrNull()
            val value = ItemsRequestModelFields.Price(min, max, selectedItem?.id)
            field.value = if (value.isEmpty()) null else value
        }

        maxValue.doOnTextChanged { _, _, _, _ ->
            val min = minValue.text?.toString()?.toIntOrNull()
            val max = maxValue.text?.toString()?.toIntOrNull()
            val value = ItemsRequestModelFields.Price(min, max, selectedItem?.id)
            field.value = if (value.isEmpty()) null else value
        }

        if (field.value != null) {
            val value = field.value as ItemsRequestModelFields.Price
            if (!value.option.isNullOrBlank()) {
                val valueText =
                    (item.dropDownValues?.singleOrNull { s -> (s as IEnum).id == value.option }) as IEnum
                filterDropDown.setText(valueText.text, false)
            }
            if (value.min != null) {
                minValue.setText(value.min.toString())
            }
            if (value.max != null) {
                maxValue.setText(value.max.toString())
            }
        }
    }
}