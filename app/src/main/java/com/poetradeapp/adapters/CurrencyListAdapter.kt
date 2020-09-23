package com.poetradeapp.adapters

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.poetradeapp.MainActivity
import com.example.poetradeapp.R
import com.google.android.material.button.MaterialButton
import com.poetradeapp.RealmCurrencyGroupData
import com.poetradeapp.flexbox.*
import com.poetradeapp.ui.SlideUpDownAnimator

class CurrencyListAdapter(
    private val items: List<RealmCurrencyGroupData>,
    private val fromWant: Boolean = false
) :
    RecyclerView.Adapter<CurrencyListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyListViewHolder {
        val context = parent.context as MainActivity
        val view = LayoutInflater.from(context).inflate(R.layout.currency_group, parent, false)
        return CurrencyListViewHolder(view, parent.context)
    }

    override fun onBindViewHolder(holder: CurrencyListViewHolder, position: Int) {
        holder.bind(items[position], fromWant)
    }

    override fun getItemCount() = items.size
}

class CurrencyListViewHolder(itemView: View, context: Context) : RecyclerView.ViewHolder(itemView) {
    private val button: MaterialButton = itemView.findViewById(R.id.currencyGroupButton)
    private val currencyGroup: RecyclerView = itemView.findViewById(R.id.currencyGroup)
    private val currencyGroupLayout: ConstraintLayout =
        itemView.findViewById(R.id.currencyGroupLayout)
    private val animator: SlideUpDownAnimator
    private var flexboxLayoutManager: FlexboxLayoutManager

    init {
        animator = SlideUpDownAnimator(currencyGroupLayout)

        flexboxLayoutManager = FlexboxLayoutManager(context)
        flexboxLayoutManager.flexWrap = FlexWrap.WRAP
        flexboxLayoutManager.justifyContent = JustifyContent.CENTER
        flexboxLayoutManager.flexDirection = FlexDirection.ROW

        currencyGroup.setHasFixedSize(true)
        currencyGroup.setItemViewCacheSize(100)

        currencyGroup.layoutManager = flexboxLayoutManager

        button.setOnClickListener {
            if (currencyGroupLayout.visibility == View.VISIBLE)
                animator.slideUp()
            else
                animator.slideDown()
        }
    }

    fun bind(group: RealmCurrencyGroupData, fromWant: Boolean) {
        val adapter = CurrencyGroupAdapter(group.currencies, fromWant)
        adapter.setHasStableIds(true)
        currencyGroup.adapter = adapter

        button.text = group.id

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