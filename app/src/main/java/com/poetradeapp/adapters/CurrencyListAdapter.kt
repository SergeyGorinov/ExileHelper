package com.poetradeapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.poetradeapp.R
import com.google.android.flexbox.*
import com.google.android.material.button.MaterialButton
import com.poetradeapp.http.RequestService
import com.poetradeapp.models.StaticData
import com.poetradeapp.models.StaticEntries
import com.poetradeapp.ui.SlideUpDownAnimator

class CurrencyListAdapter(
    private val items: List<StaticEntries>,
    private val context: Context,
    private val retrofit: RequestService
) :
    RecyclerView.Adapter<CurrencyListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyListViewHolder {
        val view =
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.currency_group, parent, false)

        return CurrencyListViewHolder(view)
    }

    override fun onBindViewHolder(holder: CurrencyListViewHolder, position: Int) {
        holder.bind(items[position].entries, context, retrofit)
    }

    override fun getItemCount() = items.size

}

class CurrencyListViewHolder : RecyclerView.ViewHolder {
    private val button: MaterialButton
    private val currencyGroup: RecyclerView
    private val animator: SlideUpDownAnimator
    private var itemDecoration: FlexboxItemDecoration? = null
    private var flexboxLayoutManager: FlexboxLayoutManager? = null


    constructor(itemView: View) : super(itemView) {
        button = itemView.findViewById(R.id.currencyGroupButton)
        currencyGroup = itemView.findViewById(R.id.currencyGroup)
        animator = SlideUpDownAnimator(currencyGroup)

    }

    fun bind(currencies: List<StaticData>, context: Context, retrofit: RequestService) {

        button.setOnClickListener {
            if (currencyGroup.visibility == View.VISIBLE)
                animator.slideUp()
            else
                animator.slideDown()
        }

        itemDecoration?.let {
            currencyGroup.addItemDecoration(it)
        } ?: run {
            itemDecoration = FlexboxItemDecoration(context)
            itemDecoration?.setOrientation(FlexboxItemDecoration.BOTH)
            itemDecoration?.setDrawable(
                context.resources.getDrawable(
                    R.drawable.flexbox_recyclerview_divider,
                    context.theme
                )
            )
            currencyGroup.addItemDecoration(itemDecoration as FlexboxItemDecoration)
        }

        flexboxLayoutManager?.let {
            currencyGroup.layoutManager = it
        } ?: run {
            flexboxLayoutManager = FlexboxLayoutManager(context)
            flexboxLayoutManager?.flexWrap = FlexWrap.WRAP
            flexboxLayoutManager?.justifyContent = JustifyContent.CENTER
            flexboxLayoutManager?.flexDirection = FlexDirection.ROW
            currencyGroup.layoutManager = flexboxLayoutManager
        }

        currencyGroup.adapter = CurrencyGroupAdapter(context, currencies, retrofit)
    }
}