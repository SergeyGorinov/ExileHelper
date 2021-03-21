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
import com.poe.tradeapp.charts_feature.databinding.ItemOverviewItemBinding
import com.poe.tradeapp.charts_feature.presentation.models.ItemOverviewViewData
import com.poe.tradeapp.core.presentation.CenteredImageSpan
import com.poe.tradeapp.core.presentation.dp
import com.squareup.picasso.Picasso
import java.lang.ref.WeakReference
import kotlin.math.roundToInt

internal class ItemOverviewAdapter(
    private val items: List<ItemOverviewViewData>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<ItemOverviewAdapter.ItemOverviewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemOverviewViewHolder {
        return ItemOverviewViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_overview_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemOverviewViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onItemClick(item.itemId)
        }
    }

    override fun getItemCount() = items.size

    internal class ItemOverviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val viewBinding = ItemOverviewItemBinding.bind(itemView)
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

        fun bind(item: ItemOverviewViewData) {
            val exchangeText =
                SpannableStringBuilder(String.format("%.1f", item.chaosEquivalentValue)).apply {
                    val image = generateChaosIcon() ?: return@apply
                    append("  ")
                    setSpan(
                        CenteredImageSpan(image),
                        length - 1,
                        length,
                        SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
            if (item.itemSparkLine != null) {
                viewBinding.chart.data = LineData(item.itemSparkLine.apply {
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
            } else {
                viewBinding.chart.visibility = View.INVISIBLE
            }
            viewBinding.label.text = item.itemName
            viewBinding.exchangeText.text = exchangeText
            viewBinding.totalChange.text = if (item.totalChange > 0) {
                "+${item.totalChange.roundToInt()}%"
            } else {
                "${item.totalChange.roundToInt()}%"
            }
            viewBinding.totalChange.setTextColor(
                when {
                    item.totalChange > 0 -> Color.GREEN
                    item.totalChange < 0 -> Color.RED
                    else -> Color.GRAY
                }
            )
            Picasso.get().load(item.itemIcon).into(viewBinding.image)
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