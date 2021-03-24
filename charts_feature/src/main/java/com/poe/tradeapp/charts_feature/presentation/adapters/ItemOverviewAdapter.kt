package com.poe.tradeapp.charts_feature.presentation.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.data.LineData
import com.poe.tradeapp.charts_feature.R
import com.poe.tradeapp.charts_feature.databinding.OverviewItemBinding
import com.poe.tradeapp.charts_feature.presentation.models.OverviewViewData
import com.squareup.picasso.Picasso
import kotlin.math.roundToInt

internal class ItemOverviewAdapter(
    private val items: List<OverviewViewData>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<ItemOverviewAdapter.ItemOverviewViewHolder>() {

    private val picassoInstance = Picasso.get()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemOverviewViewHolder {
        return ItemOverviewViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.overview_item, parent, false
            ), picassoInstance
        )
    }

    override fun onBindViewHolder(holder: ItemOverviewViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onItemClick(item.id)
        }
    }

    override fun getItemCount() = items.size

    internal class ItemOverviewViewHolder(itemView: View, private val picassoInstance: Picasso) :
        RecyclerView.ViewHolder(itemView) {

        private val viewBinding = OverviewItemBinding.bind(itemView)

        init {
            viewBinding.chart.apply {
                description.isEnabled = false
                legend.isEnabled = false
                xAxis.isEnabled = false
                axisLeft.isEnabled = false
                axisRight.isEnabled = false
                setTouchEnabled(false)
                setDrawGridBackground(false)
            }
        }

        fun bind(item: OverviewViewData) {
            viewBinding.chart.data = LineData(item.sellingSparkLine.apply {
                color =
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.primaryLineChartColor
                    )
                fillColor = ContextCompat.getColor(
                    itemView.context,
                    R.color.primaryLineChartFillColor
                )
            })
            viewBinding.label.text = item.name
            viewBinding.exchangeText.text = itemView.context.getString(
                R.string.exchange_text, 1f, item.sellingListingData.value
            )
            viewBinding.totalChange.text = if (item.sellingTotalChange > 0) {
                "+${item.sellingTotalChange.roundToInt()}%"
            } else {
                "${item.sellingTotalChange.roundToInt()}%"
            }
            viewBinding.totalChange.setTextColor(
                when {
                    item.sellingTotalChange > 0 -> Color.GREEN
                    item.sellingTotalChange < 0 -> Color.RED
                    else -> Color.GRAY
                }
            )
            picassoInstance.load(item.icon).fit().into(viewBinding.leftSideImage)
            picassoInstance.load(R.drawable.chaos_icon).fit().into(viewBinding.rightSideImage)
            picassoInstance.load(item.icon).into(viewBinding.image)
        }
    }
}