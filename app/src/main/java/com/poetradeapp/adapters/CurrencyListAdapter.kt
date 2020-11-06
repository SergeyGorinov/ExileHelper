package com.poetradeapp.adapters

import android.content.Context
import android.content.res.Resources
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.poetradeapp.R
import com.poetradeapp.flexbox.FlexDirection
import com.poetradeapp.flexbox.FlexWrap
import com.poetradeapp.flexbox.FlexboxLayoutManager
import com.poetradeapp.flexbox.JustifyContent
import com.poetradeapp.models.ui.StaticGroupViewData
import com.poetradeapp.ui.SlideUpDownAnimator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*

@ExperimentalCoroutinesApi
class CurrencyListAdapter(
    private val items: ArrayList<StaticGroupViewData>,
    private val fromWant: Boolean = false
) :
    RecyclerView.Adapter<CurrencyListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyListViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.currency_group, parent, false)
        return CurrencyListViewHolder(view, parent.context)
    }

    override fun onBindViewHolder(holder: CurrencyListViewHolder, position: Int) {
        holder.bind(items[position], fromWant)
    }

    override fun getItemCount() = items.size
}

@ExperimentalCoroutinesApi
class CurrencyListViewHolder(itemView: View, context: Context) : RecyclerView.ViewHolder(itemView) {
    private val button: MaterialButton = itemView.findViewById(R.id.currencyGroupButton)
    private val currencyGroup: RecyclerView = itemView.findViewById(R.id.currencyGroup)
    private val currencyGroupLayout: LinearLayout =
        itemView.findViewById(R.id.currencyGroupLayout)
    private val currencyGroupSearch: TextInputEditText =
        itemView.findViewById(R.id.currencyGroupSearch)
    private val currencyGroupSearchLayout: LinearLayout =
        itemView.findViewById(R.id.currencyGroupSearchLayout)
    private val animator: SlideUpDownAnimator
    private var flexboxLayoutManager: FlexboxLayoutManager

    init {
        animator = SlideUpDownAnimator(currencyGroupLayout)

        flexboxLayoutManager = FlexboxLayoutManager(context)
        flexboxLayoutManager.flexWrap = FlexWrap.WRAP
        flexboxLayoutManager.justifyContent = JustifyContent.CENTER
        flexboxLayoutManager.flexDirection = FlexDirection.ROW

        currencyGroup.layoutManager = flexboxLayoutManager

        button.setOnClickListener {
            if (currencyGroupLayout.visibility == View.VISIBLE) {
                animator.slideUp()
            } else {
                animator.slideDown()
            }
        }
    }

    fun bind(group: StaticGroupViewData, fromWant: Boolean) {
        when (group.id) {
            "Cards" -> {
                val adapter = CardsGroupAdapter(fromWant)

                button.setOnClickListener {
                    if (currencyGroupLayout.visibility == View.VISIBLE) {
                        animator.slideUp()
                    } else {
                        animator.slideDown()
                    }
                }

                currencyGroup.setItemViewCacheSize(50)
                currencyGroup.adapter = adapter

                currencyGroupSearchLayout.visibility = View.VISIBLE
                currencyGroupSearch.hint =
                    itemView.context.getString(R.string.cards_group_search_hint)
                currencyGroupSearch.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) =
                        Unit

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

                    override fun afterTextChanged(p0: Editable?) {
                        if (p0 != null && p0.length > 2) {
                            val newItems =
                                group.staticItems.filter { f ->
                                    f.label
                                        .toLowerCase(Locale.getDefault())
                                        .contains(
                                            p0.toString().toLowerCase(Locale.getDefault())
                                        )
                                }
                            if (newItems.size > 50)
                                adapter.updateItems(newItems.subList(0, 50))
                            else
                                adapter.updateItems(newItems)
                        } else
                            adapter.updateItems(listOf())
                    }
                })
            }
            "Maps" -> {
                val adapter = MapsGroupAdapter(fromWant)

                button.setOnClickListener {
                    if (currencyGroupLayout.visibility == View.VISIBLE) {
                        animator.slideUp()
                    } else {
                        animator.slideDown()
                    }
                }

                currencyGroup.setItemViewCacheSize(20)
                currencyGroup.adapter = adapter

                currencyGroupSearchLayout.visibility = View.VISIBLE
                currencyGroupSearch.hint =
                    itemView.context.getString(R.string.maps_group_search_hint)
                currencyGroupSearch.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) =
                        Unit

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

                    override fun afterTextChanged(p0: Editable?) {
                        if (p0 != null && p0.length > 2) {
                            val newItems =
                                group.staticItems.filter { f ->
                                    f.label
                                        .toLowerCase(Locale.getDefault())
                                        .contains(p0.toString().toLowerCase(Locale.getDefault()))
                                }.groupBy { g -> g.groupLabel }
                            adapter.updateItems(newItems)
                        } else
                            adapter.updateItems(mapOf())
                    }
                })
            }
            else -> {
                val adapter = CurrencyGroupAdapter(group.staticItems, fromWant)
                currencyGroup.setItemViewCacheSize(100)
                currencyGroup.adapter = adapter
            }
        }

        button.text = group.label

        val layoutParams = currencyGroupLayout.layoutParams
        layoutParams.height = 1
        currencyGroupLayout.layoutParams = layoutParams

        currencyGroupLayout.measure(
            View.MeasureSpec.makeMeasureSpec(
                Resources.getSystem().displayMetrics.widthPixels,
                View.MeasureSpec.EXACTLY
            ),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )

        animator.setHeight(currencyGroupLayout.measuredHeight)
    }
}