package com.sgorinov.exilehelper.charts_feature.presentation.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.data.LineData
import com.sgorinov.exilehelper.charts_feature.R
import com.sgorinov.exilehelper.charts_feature.databinding.OverviewItemBinding
import com.sgorinov.exilehelper.charts_feature.presentation.models.OverviewViewData
import com.squareup.picasso.Picasso
import kotlin.math.roundToInt

internal class OverviewAdapter(private val onItemClick: (String) -> Unit) :
    RecyclerView.Adapter<OverviewAdapter.CurrencyOverviewViewHolder>() {

    var selling = true
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private val diffUtilCallback = object : DiffUtil.ItemCallback<OverviewViewData>() {
        override fun areItemsTheSame(
            oldItem: OverviewViewData,
            newItem: OverviewViewData
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: OverviewViewData,
            newItem: OverviewViewData
        ): Boolean {
            return oldItem == newItem
        }
    }

    private val picassoInstance = Picasso.get()
    private val asyncDiffer = AsyncListDiffer(this, diffUtilCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyOverviewViewHolder {
        return CurrencyOverviewViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.overview_item, parent, false), picassoInstance
        )
    }

    override fun onBindViewHolder(holder: CurrencyOverviewViewHolder, position: Int) {
        val item = asyncDiffer.currentList[position]
        holder.bind(item, selling)
        holder.itemView.setOnClickListener {
            onItemClick(item.id)
        }
    }

    override fun getItemCount() = asyncDiffer.currentList.size

    fun setData(data: List<OverviewViewData>) {
        asyncDiffer.submitList(data)
    }

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

        fun bind(item: OverviewViewData, selling: Boolean) {
            val totalChangeValue = if (selling) {
                item.sellingTotalChange?.roundToInt()
            } else {
                item.buyingTotalChange.roundToInt()
            } ?: 0
            setupChart(item, selling)
            viewBinding.label.text = item.name
            viewBinding.exchangeText.text = if (selling) {
                itemView.context.getString(
                    R.string.exchange_text,
                    1f,
                    item.sellingListingData?.value?.toFloat() ?: 0f
                )
            } else {
                itemView.context.getString(
                    R.string.exchange_text,
                    item.buyingListingData.value.toFloat(),
                    1f
                )
            }
            viewBinding.totalChange.text = if (totalChangeValue > 0) {
                "+$totalChangeValue%"
            } else {
                "$totalChangeValue%"
            }
            viewBinding.totalChange.setTextColor(
                when {
                    totalChangeValue > 0 -> Color.GREEN
                    totalChangeValue < 0 -> Color.RED
                    else -> Color.GRAY
                }
            )
            picassoInstance.load(item.icon).fit().into(
                if (selling) viewBinding.leftSideImage else viewBinding.rightSideImage
            )
            picassoInstance.load(R.drawable.chaos_icon).fit().into(
                if (selling) viewBinding.rightSideImage else viewBinding.leftSideImage
            )
            picassoInstance.load(item.icon).fit().into(viewBinding.image)
        }

        private fun setupChart(item: OverviewViewData, selling: Boolean) {
            when {
                !selling -> {
                    viewBinding.chart.data = LineData(item.buyingSparkLine.apply {
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
                selling && item.sellingSparkLine == null -> {
                    viewBinding.chart.visibility = View.INVISIBLE
                }
                selling && item.sellingSparkLine != null -> {
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
                }
            }
        }
    }
}