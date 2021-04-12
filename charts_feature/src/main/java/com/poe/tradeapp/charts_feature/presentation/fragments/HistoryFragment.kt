package com.poe.tradeapp.charts_feature.presentation.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.renderer.LineChartRenderer
import com.github.mikephil.charting.utils.MPPointF
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.poe.tradeapp.charts_feature.R
import com.poe.tradeapp.charts_feature.databinding.FragmentHistoryBinding
import com.poe.tradeapp.charts_feature.presentation.models.HistoryModel
import com.poe.tradeapp.core.presentation.BaseFragment
import com.poe.tradeapp.core.presentation.dpf
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

internal class HistoryFragment : BaseFragment(R.layout.fragment_history) {

    private val isCurrency by lazy { requireArguments().getBoolean(IS_CURRENCY_KEY, false) }

    private lateinit var binding: FragmentHistoryBinding

    internal var data: HistoryModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding = FragmentHistoryBinding.bind(view)
        binding = getBinding()

        if (data == null) {
            router.exit()
            return
        }

        val textColor = ContextCompat.getColor(
            requireActivity(),
            R.color.primaryTextColor
        )
        val textFont = ResourcesCompat.getFont(
            requireActivity(),
            com.poe.tradeapp.core.R.font.fontinsmallcaps
        )
        data?.let { data ->
            setupChartData(data.buyingGraphData, false, textColor, textFont)
            setupChartData(data.sellingGraphData, true, textColor, textFont)
            setupChart(data, textColor, textFont)
            binding.toolbarLayout.toolbar.title = "Item history"
            binding.itemLabel.text = data.name
            binding.addNotification.setOnClickListener {
                goToExchange(data, true)
            }
            binding.goToWiki.setOnClickListener {
                createWikiDialog(data.name).show()
            }
            binding.buyButton.setOnClickListener {
                goToExchange(data, false)
            }
            binding.leftSideBuyText.text = getString(R.string.buy_chart_text, data.name)
            binding.rightSideBuyText.text = getString(R.string.receive_chart_text, data.buyingValue)
            if (isCurrency) {
                binding.sellData.visibility = View.VISIBLE
                binding.sellButton.visibility = View.VISIBLE
                binding.leftSideSellText.text = getString(R.string.sell_chart_text, data.name)

                binding.rightSideSellText.text =
                    getString(R.string.receive_chart_text, data.sellingValue ?: 0f)
                binding.sellButton.setOnClickListener {
                    getMainActivity()?.goToCurrencyExchange("chaos", data.tradeId)
                }
                Picasso.get().load(data.icon).fit().into(binding.leftSideSellImage)
            }
            Picasso.get().load(data.icon).fit().into(binding.leftSideBuyImage)
            Picasso.get().load(data.icon).fit().into(binding.itemImage)
        }
    }

    private fun setupChartData(
        chartData: LineDataSet?,
        selling: Boolean,
        textColor: Int,
        textFont: Typeface?
    ) {
        chartData ?: return
        val currentColor = ContextCompat.getColor(
            requireActivity(),
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
        val xMax = maxOf(data.sellingGraphData?.xMax ?: 0f, data.buyingGraphData.xMax)
        val xMin = minOf(data.sellingGraphData?.xMin ?: 0f, data.buyingGraphData.xMin)
        binding.chart.xAxis.apply {
            axisMinimum = xMin
            axisMaximum = xMax + 5f
            position = XAxis.XAxisPosition.BOTTOM
            labelRotationAngle = 45f
            textSize = 16f
            typeface = textFont
            this.textColor = textColor
            valueFormatter = object : ValueFormatter() {
                override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                    val currentDate = Calendar.getInstance()
                    currentDate.add(Calendar.DAY_OF_YEAR, -(xMax - value).toInt())
                    return SimpleDateFormat("MMM dd", Locale.getDefault()).format(currentDate.time)
                }
            }
        }
        binding.chart.axisLeft.apply {
            xOffset = 25f
            textSize = 16f
            typeface = textFont
            this.textColor = textColor
        }
        binding.chart.apply {
            marker = CustomMarkerView(requireActivity(), R.layout.custom_marker_view)
            description.isEnabled = false
            legend.isEnabled = false
            axisRight.isEnabled = false
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

    private fun createWikiDialog(path: String): AlertDialog {
        return AlertDialog.Builder(requireActivity())
            .setPositiveButton("Yes") { _, _ ->
                val uri = Uri.Builder()
                    .scheme("https")
                    .authority("pathofexile.gamepedia.com")
                    .appendPath(path)
                    .build()
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = uri
                }
                requireActivity().startActivity(intent)
            }
            .setNegativeButton("No") { _, _ ->
                return@setNegativeButton
            }
            .setTitle("Open page on PoeWiki?")
            .create()
    }

    private fun goToExchange(data: HistoryModel, withNotification: Boolean) {
        if (isCurrency) {
            getMainActivity()?.goToCurrencyExchange(data.tradeId, "chaos", false, withNotification)
        } else {
            if (data.type == null) {
                getMainActivity()?.goToItemsSearch(data.name, null, withNotification)
            } else {
                getMainActivity()?.goToItemsSearch(data.type, data.name, withNotification)
            }
        }
    }

    private class CustomMarkerView(context: Context, resId: Int) : MarkerView(context, resId) {

        override fun refreshContent(e: Entry?, highlight: Highlight?) {
            e?.y?.let {
                findViewById<TextView>(R.id.tvContent).text = String.format("%.1f", it)
            }
            super.refreshContent(e, highlight)
        }

        override fun getOffset(): MPPointF {
            val xOffset = -(width / 2)
            val yOffset = -height - 10
            return MPPointF(xOffset.toFloat(), yOffset.toFloat())
        }
    }

    companion object {
        private const val IS_CURRENCY_KEY = "IS_CURRENCY_KEY"

        fun newInstance(isCurrency: Boolean, data: HistoryModel): FragmentScreen {
            return FragmentScreen {
                HistoryFragment().apply {
                    this.data = data
                    arguments = bundleOf(IS_CURRENCY_KEY to isCurrency)
                }
            }
        }
    }
}