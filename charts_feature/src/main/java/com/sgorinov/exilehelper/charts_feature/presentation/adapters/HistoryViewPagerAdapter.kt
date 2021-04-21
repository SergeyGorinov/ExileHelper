package com.sgorinov.exilehelper.charts_feature.presentation.adapters

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.renderer.LineChartRenderer
import com.sgorinov.exilehelper.charts_feature.R
import com.sgorinov.exilehelper.charts_feature.databinding.HistoryFragmentChartsViewBinding
import com.sgorinov.exilehelper.charts_feature.databinding.HistoryFragmentInfoViewBinding
import com.sgorinov.exilehelper.charts_feature.presentation.CustomMarkerView
import com.sgorinov.exilehelper.charts_feature.presentation.models.HistoryModel
import com.sgorinov.exilehelper.core.presentation.dpf
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

internal class HistoryViewPagerAdapter(
    private val data: HistoryModel,
    private val isCurrency: Boolean,
    private val onExchangeClick: (Boolean, Boolean, String, String?) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val resId = if (viewType == INFO_VIEW_TYPE) {
            R.layout.history_fragment_info_view
        } else {
            R.layout.history_fragment_charts_view
        }
        val view = LayoutInflater.from(parent.context).inflate(resId, parent, false)
        return if (viewType == INFO_VIEW_TYPE) {
            InfoViewHolder(view, isCurrency, onExchangeClick)
        } else {
            ChartsViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ChartsViewHolder -> holder.bind(data)
            is InfoViewHolder -> holder.bind(data)
        }
    }

    override fun getItemCount() = 2

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) INFO_VIEW_TYPE else CHARTS_VIEW_TYPE
    }

    internal class ChartsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val viewBinding = HistoryFragmentChartsViewBinding.bind(itemView)
        private val textColor = ContextCompat.getColor(
            itemView.context,
            R.color.primaryTextColor
        )
        private val textFont = ResourcesCompat.getFont(
            itemView.context,
            R.font.fontinsmallcaps
        )

        fun bind(data: HistoryModel) {
            setupChartData(data.buyingGraphData, false, textColor, textFont)
            setupChartData(data.sellingGraphData, true, textColor, textFont)
            setupChart(data, textColor, textFont)
        }

        private fun setupChartData(
            chartData: LineDataSet?,
            selling: Boolean,
            textColor: Int,
            textFont: Typeface?
        ) {
            chartData ?: return
            val currentColor = ContextCompat.getColor(
                itemView.context,
                if (!selling) R.color.primaryLineChartColor else R.color.secondaryLineChartColor
            )
            chartData.apply {
                color = currentColor
                valueTextColor = textColor
                valueTypeface = textFont
                valueTextSize = 16f
                valueFormatter = object : ValueFormatter() {
                    override fun getPointLabel(entry: Entry): String {
                        return if (chartData.getEntryIndex(entry) % 2 == 0) {
                            entry.y.roundToInt().toString()
                        } else {
                            ""
                        }
                    }
                }
                setCircleColor(currentColor)
            }
        }

        private fun setupChart(data: HistoryModel, textColor: Int, textFont: Typeface?) {
            val xMax = maxOf(data.sellingGraphData?.xMax ?: 0f, data.buyingGraphData?.xMax ?: 0f)
            val xMin = minOf(data.sellingGraphData?.xMin ?: 0f, data.buyingGraphData?.xMin ?: 0f)
            viewBinding.chart.xAxis.apply {
                axisMinimum = xMin
                axisMaximum = xMax + 5f
                position = XAxis.XAxisPosition.BOTTOM
                labelRotationAngle = 45f
                textSize = 16f
                typeface = textFont
                setLabelCount(10, true)
                this.textColor = textColor
                valueFormatter = object : ValueFormatter() {
                    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                        val currentDate = Calendar.getInstance()
                        currentDate.add(Calendar.DAY_OF_YEAR, -(xMax - value).toInt())
                        return SimpleDateFormat(
                            "MMM dd",
                            Locale.getDefault()
                        ).format(currentDate.time)
                    }
                }
                setDrawGridLines(false)
            }
            viewBinding.chart.axisLeft.apply {
                xOffset = 25f
                textSize = 16f
                typeface = textFont
                this.textColor = textColor
                setDrawGridLines(false)
            }
            viewBinding.chart.apply {
                marker = CustomMarkerView(itemView.context, R.layout.custom_marker_view)
                description.isEnabled = false
                legend.isEnabled = false
                axisRight.isEnabled = false
                isDoubleTapToZoomEnabled = false
                setTouchEnabled(true)
                this.data = if (data.sellingGraphData != null) {
                    LineData(data.buyingGraphData, data.sellingGraphData)
                } else {
                    LineData(data.buyingGraphData)
                }
                renderer = object : LineChartRenderer(this, animator, viewPortHandler) {
                    override fun drawValue(
                        c: Canvas?,
                        valueText: String?,
                        x: Float,
                        y: Float,
                        color: Int
                    ) {
                        super.drawValue(c, valueText, x, y - 20, color)
                    }
                }
                extraBottomOffset = 8.dpf
            }
        }
    }

    internal class InfoViewHolder(
        itemView: View,
        private val isCurrency: Boolean,
        private val onExchangeClick: (Boolean, Boolean, String, String?) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private val viewBinding = HistoryFragmentInfoViewBinding.bind(itemView)

        init {
            viewBinding.buyingChart.apply {
                description.isEnabled = false
                legend.isEnabled = false
                xAxis.isEnabled = false
                axisLeft.isEnabled = false
                axisRight.isEnabled = false
                setTouchEnabled(false)
                setDrawGridBackground(false)
            }
            viewBinding.sellingChart.apply {
                description.isEnabled = false
                legend.isEnabled = false
                xAxis.isEnabled = false
                axisLeft.isEnabled = false
                axisRight.isEnabled = false
                setTouchEnabled(false)
                setDrawGridBackground(false)
            }
        }

        fun bind(data: HistoryModel) {
            viewBinding.itemLabel.text = data.name
            viewBinding.addNotification.setOnClickListener {
                goToExchange(data, true)
            }
            viewBinding.goToWiki.setOnClickListener {
                createWikiDialog(data.name).show()
            }
            viewBinding.buyButton.setOnClickListener {
                goToExchange(data, false)
            }
            viewBinding.leftSideBuyText.text =
                itemView.context.getString(R.string.buy_chart_text, data.name)
            viewBinding.rightSideBuyText.text =
                itemView.context.getString(R.string.receive_chart_text, data.buyingValue)
            setupChart(
                data,
                false,
                viewBinding.buyingChartNoData,
                viewBinding.buyingChart,
                viewBinding.buyingAdditionalDataChange
            )
            if (isCurrency) {
                viewBinding.sellData.visibility = View.VISIBLE
                viewBinding.sellButton.visibility = View.VISIBLE
                viewBinding.additionalSellData.visibility = View.VISIBLE
                viewBinding.leftSideSellText.text =
                    itemView.context.getString(R.string.sell_chart_text, data.name)

                viewBinding.rightSideSellText.text =
                    itemView.context.getString(R.string.receive_chart_text, data.sellingValue ?: 0f)
                viewBinding.sellButton.setOnClickListener {
                    onExchangeClick(true, false, "chaos", data.tradeId)
                }
                setupChart(
                    data,
                    true,
                    viewBinding.sellingChartNoData,
                    viewBinding.sellingChart,
                    viewBinding.sellingAdditionalDataChange
                )
                Picasso.get().load(data.icon).fit().into(viewBinding.leftSideSellImage)
            }
            Picasso.get().load(data.icon).fit().into(viewBinding.leftSideBuyImage)
            Picasso.get().load(data.icon).fit().into(viewBinding.itemImage)
        }

        private fun setupChart(
            item: HistoryModel,
            selling: Boolean,
            placeholder: AppCompatTextView,
            chart: LineChart,
            textView: AppCompatTextView
        ) {
            val totalChangeValue = if (selling) {
                item.sellingTotalChange?.roundToInt()
            } else {
                item.buyingTotalChange.roundToInt()
            } ?: 0
            textView.text = if (totalChangeValue > 0) {
                "+$totalChangeValue%"
            } else {
                "$totalChangeValue%"
            }
            textView.setTextColor(
                when {
                    totalChangeValue > 0 -> Color.GREEN
                    totalChangeValue < 0 -> Color.RED
                    else -> Color.GRAY
                }
            )
            when {
                !selling && item.buyingSparkLine.entryCount == 0 -> {
                    chart.visibility = View.GONE
                    placeholder.visibility = View.VISIBLE
                }
                !selling && item.buyingSparkLine.entryCount > 0 -> {
                    chart.visibility = View.VISIBLE
                    placeholder.visibility = View.GONE
                    chart.data = LineData(item.buyingSparkLine.apply {
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
                selling && (item.sellingSparkLine == null || item.sellingSparkLine.entryCount == 0) -> {
                    chart.visibility = View.GONE
                    placeholder.visibility = View.VISIBLE
                }
                selling && item.sellingSparkLine != null && item.sellingSparkLine.entryCount != 0 -> {
                    chart.visibility = View.VISIBLE
                    placeholder.visibility = View.GONE
                    chart.data = LineData(item.sellingSparkLine.apply {
                        color = ContextCompat.getColor(
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

        private fun createWikiDialog(path: String): AlertDialog {
            return AlertDialog.Builder(itemView.context)
                .setPositiveButton("Yes") { _, _ ->
                    val uri = Uri.Builder()
                        .scheme("https")
                        .authority("pathofexile.gamepedia.com")
                        .appendPath(path)
                        .build()
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = uri
                    }
                    itemView.context.startActivity(intent)
                }
                .setNegativeButton("No") { _, _ ->
                    return@setNegativeButton
                }
                .setTitle("Open page on PoeWiki?")
                .create()
        }

        private fun goToExchange(
            data: HistoryModel,
            withNotification: Boolean
        ) {
            if (isCurrency) {
                data.tradeId?.let {
                    onExchangeClick(true, withNotification, it, "chaos")
                }
            } else {
                if (data.type == null) {
                    onExchangeClick(false, withNotification, data.name, null)
                } else {
                    onExchangeClick(false, withNotification, data.type, data.name)
                }
            }
        }
    }

    companion object {
        private const val INFO_VIEW_TYPE = 0
        private const val CHARTS_VIEW_TYPE = 1
    }
}