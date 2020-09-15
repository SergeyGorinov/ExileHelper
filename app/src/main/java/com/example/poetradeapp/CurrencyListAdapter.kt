package com.example.poetradeapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder

class CurrencyGroupViewHolder(itemView: View) : GroupViewHolder(itemView) {
    private val currencyGroupId = itemView.findViewById<TextView>(R.id.CurrencyGroupId)
    private val currencyGroupDescription =
        itemView.findViewById<TextView>(R.id.CurrencyGroupDescription)

    fun bindData(currencyGroup: MainActivity.CurrencyGroup) {
        currencyGroupId.text = currencyGroup.id
        currencyGroupDescription.text = currencyGroup.description
    }
//    override fun expand() {
//        animate(true)
//    }
//
//    override fun collapse() {
//        animate(false)
//    }
//
//    private fun animate(isExpand: Boolean) {
//        val fromDegrees = if(isExpand) 360f else 180f
//        val toDegrees = if(isExpand) 180f else 360f
//        val rotate = RotateAnimation(fromDegrees, toDegrees, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f)
//        rotate.duration = 300
//        rotate.fillAfter = true
//        currencyGroupId.animation = rotate
//    }
}

class CurrencyViewHolder(itemView: View) : ChildViewHolder(itemView) {
    private val currencyId = itemView.findViewById<TextView>(R.id.CurrencyId)
    private val currencyDescription = itemView.findViewById<TextView>(R.id.CurrencyDescription)
    private val currencyImage = itemView.findViewById<ImageView>(R.id.CurrencyImage)

    fun bindData(currency: MainActivity.Currency) {
        currencyId.text = currency.id
        currencyDescription.text = currency.text
        currencyImage.setImageBitmap(currency.image)
    }
}

class CurrencyGroupListAdapter(var currencyGroups: MutableList<ExpandableGroup<*>?>?) :
    ExpandableRecyclerViewAdapter<CurrencyGroupViewHolder, CurrencyViewHolder>(currencyGroups) {

    override fun onCreateGroupViewHolder(
        parent: ViewGroup?,
        viewType: Int
    ): CurrencyGroupViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.currency_group, parent, false)
        return CurrencyGroupViewHolder(view)
    }

    override fun onCreateChildViewHolder(parent: ViewGroup?, viewType: Int): CurrencyViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.currency, parent, false)
        return CurrencyViewHolder(view)
    }

    override fun onBindGroupViewHolder(
        holder: CurrencyGroupViewHolder?,
        flatPosition: Int,
        group: ExpandableGroup<*>?
    ) {
        val currencyGroup = (group as MainActivity.CurrencyGroup)
        holder?.bindData(currencyGroup)
    }

    override fun onBindChildViewHolder(
        holder: CurrencyViewHolder?,
        flatPosition: Int,
        group: ExpandableGroup<*>?,
        childIndex: Int
    ) {
        val currency = (group as MainActivity.CurrencyGroup).currencies[childIndex]
        holder?.bindData(currency)
    }

    fun addData(index: Int, item: ExpandableGroup<*>?) {
        currencyGroups?.set(index, item)
        this.notifyDataSetChanged()
    }
}