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

internal class CurrencyOverviewAdapter(
    private val items: List<OverviewViewData>,
    private val isBuy: Boolean,
    private val onItemClick: (String) -> Unit,
    private val onWikiClick: (String) -> Unit
) : RecyclerView.Adapter<CurrencyOverviewAdapter.CurrencyOverviewViewHolder>() {

    private val picassoInstance = Picasso.get()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyOverviewViewHolder {
        return CurrencyOverviewViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.overview_item, parent, false), picassoInstance
        )
    }

    override fun onBindViewHolder(holder: CurrencyOverviewViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item, isBuy, onWikiClick)
        holder.itemView.setOnClickListener {
            onItemClick(item.id)
        }
    }

    override fun getItemCount() = items.size

    internal class CurrencyOverviewViewHolder(
        itemView: View,
        private val picassoInstance: Picasso
    ) : RecyclerView.ViewHolder(itemView) {

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

        fun bind(item: OverviewViewData, selling: Boolean, onWikiClick: (String) -> Unit) {
            val totalChangeValue = if (selling) {
                item.buyingTotalChange?.roundToInt()
            } else {
                item.sellingTotalChange.roundToInt()
            } ?: 0
            setupChart(item, selling)
            viewBinding.label.text = item.name
            if (selling) {
                viewBinding.exchangeText.text = itemView.context.getString(
                    R.string.exchange_text,
                    1f,
                    item.sellingListingData.value.toFloat()
                )
            } else {
                val price = 1 / (item.buyingListingData?.value?.toFloat() ?: -1f)
                viewBinding.exchangeText.text = itemView.context.getString(
                    R.string.exchange_text, if (price > 0) price else 0f, 1f
                )
            }
            viewBinding.totalChange.text = when {
                totalChangeValue > 0 -> "+$totalChangeValue%"
                totalChangeValue < 0 -> "-$totalChangeValue%"
                else -> "$totalChangeValue%"
            }
            viewBinding.totalChange.setTextColor(
                when {
                    totalChangeValue > 0 -> Color.GREEN
                    totalChangeValue < 0 -> Color.RED
                    else -> Color.GRAY
                }
            )
            viewBinding.goToWiki.setOnClickListener {
                onWikiClick(item.name)
            }
            picassoInstance.load(item.icon).fit().into(
                if (selling) viewBinding.leftSideImage else viewBinding.rightSideImage
            )
            picassoInstance.load(R.drawable.chaos_icon).fit().into(
                if (selling) viewBinding.rightSideImage else viewBinding.leftSideImage
            )
            picassoInstance.load(item.icon).fit().into(viewBinding.image)
        }

        private fun setupChart(item: OverviewViewData, isBuying: Boolean) {
            when {
                isBuying && item.buyingSparkLine == null -> {
                    viewBinding.chart.visibility = View.INVISIBLE
                }
                isBuying && item.buyingSparkLine != null -> {
                    viewBinding.chart.data = LineData(item.buyingSparkLine.apply {
                        color =
                            ContextCompat.getColor(itemView.context, R.color.primaryLineChartColor)
                        fillColor = ContextCompat.getColor(
                            itemView.context,
                            R.color.primaryLineChartFillColor
                        )
                    })
                }
                !isBuying -> {
                    viewBinding.chart.data = LineData(item.sellingSparkLine.apply {
                        color =
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.secondaryLineChartColor
                            )
                        fillColor = ContextCompat.getColor(
                            itemView.context,
                            R.color.secondaryLineChartFillColor
                        )
                    })
                }
            }
        }
    }
}