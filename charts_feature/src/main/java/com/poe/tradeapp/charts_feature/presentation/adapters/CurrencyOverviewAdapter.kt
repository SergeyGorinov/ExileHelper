package com.poe.tradeapp.charts_feature.presentation.adapters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.data.LineData
import com.poe.tradeapp.charts_feature.R
import com.poe.tradeapp.charts_feature.databinding.CurrencyOverviewItemBinding
import com.poe.tradeapp.charts_feature.presentation.models.CurrencyOverviewViewData
import com.poe.tradeapp.core.presentation.CenteredImageSpan
import com.poe.tradeapp.core.presentation.dp
import com.squareup.picasso.Picasso
import java.lang.ref.WeakReference
import kotlin.math.roundToInt

internal class CurrencyOverviewAdapter(
    private val items: List<CurrencyOverviewViewData>,
    private val isBuy: Boolean,
    private val onItemClick: (String) -> Unit,
    private val onWikiClick: (String) -> Unit
) :
    RecyclerView.Adapter<CurrencyOverviewAdapter.CurrencyOverviewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyOverviewViewHolder {
        return CurrencyOverviewViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.currency_overview_item, parent, false)
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

    internal class CurrencyOverviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val viewBinding = CurrencyOverviewItemBinding.bind(itemView)
        private var chaosIcon = WeakReference<Drawable>(null)

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

        fun bind(item: CurrencyOverviewViewData, isBuying: Boolean, onWikiClick: (String) -> Unit) {
            val exchangeText = SpannableStringBuilder()
            val totalChangeValue = if (isBuying) {
                item.chaosEquivalentTotalChange.roundToInt()
            } else {
                item.currencyTotalChange.roundToInt()
            }
            setupChart(item, isBuying)
            setupExchangeText(item, isBuying, exchangeText)
            viewBinding.label.text = item.currencyName
            viewBinding.exchangeText.text = exchangeText
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
                onWikiClick(item.currencyName)
            }
            Picasso.get().load(item.currencyIcon).into(viewBinding.image)
        }

        private fun setupChart(item: CurrencyOverviewViewData, isBuying: Boolean) {
            when {
                isBuying && item.chaosEquivalentSparkLine == null -> {
                    viewBinding.chart.visibility = View.INVISIBLE
                }
                !isBuying && item.currencySparkLine == null -> {
                    viewBinding.chart.visibility = View.INVISIBLE
                }
                isBuying && item.chaosEquivalentSparkLine != null -> {
                    viewBinding.chart.data = LineData(item.chaosEquivalentSparkLine.apply {
                        color =
                            ContextCompat.getColor(itemView.context, R.color.primaryLineChartColor)
                        fillColor = ContextCompat.getColor(
                            itemView.context,
                            R.color.primaryLineChartFillColor
                        )
                    })
                }
                !isBuying && item.currencySparkLine != null -> {
                    viewBinding.chart.data = LineData(item.currencySparkLine.apply {
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

        private fun setupExchangeText(
            item: CurrencyOverviewViewData,
            isBuying: Boolean,
            text: SpannableStringBuilder
        ) {
            val leftSideText = if (isBuying) {
                SpannableStringBuilder(String.format("%.1f", item.chaosEquivalentData?.value ?: 0f))
            } else {
                SpannableStringBuilder("1.0")
            }.apply {
                append("  ")
            }
            val rightSideText = if (isBuying) {
                SpannableStringBuilder("1.0")
            } else {
                SpannableStringBuilder(
                    String.format("%.1f", item.currencyData?.value?.let { 1 / it } ?: 0.0)
                )
            }.apply {
                append("  ")
            }
            val leftSideIcon = if (isBuying) generateChaosIcon() else item.currencyIconForText
            val rightSideIcon = if (isBuying) item.currencyIconForText else generateChaosIcon()
            if (leftSideIcon != null) {
                leftSideText.setSpan(
                    CenteredImageSpan(leftSideIcon),
                    leftSideText.length - 1,
                    leftSideText.length,
                    SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            if (rightSideIcon != null) {
                rightSideText.setSpan(
                    CenteredImageSpan(rightSideIcon),
                    rightSideText.length - 1,
                    rightSideText.length,
                    SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            text.append(leftSideText)
            text.append(" -> ")
            text.append(rightSideText)
        }

        private fun generateChaosIcon(): Drawable? {
            if (chaosIcon.get() == null) {
                chaosIcon = WeakReference(
                    BitmapFactory.decodeResource(
                        itemView.resources,
                        R.drawable.chaos_icon
                    ).run {
                        BitmapDrawable(
                            itemView.resources,
                            Bitmap.createScaledBitmap(this, 24.dp, 24.dp, true)
                        ).apply {
                            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
                        }
                    })
                return chaosIcon.get()
            } else {
                return chaosIcon.get()
            }
        }
    }
}