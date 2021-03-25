package com.poe.tradeapp.charts_feature.presentation.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.poe.tradeapp.charts_feature.R
import com.poe.tradeapp.charts_feature.databinding.FragmentHistoryBinding
import com.poe.tradeapp.charts_feature.presentation.ChartsViewModel
import com.poe.tradeapp.charts_feature.presentation.models.HistoryModel
import com.poe.tradeapp.core.presentation.BaseFragment
import com.poe.tradeapp.core.presentation.getTransparentProgressDialog
import com.squareup.picasso.Picasso
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

internal class HistoryFragment : BaseFragment(R.layout.fragment_history) {

    private val viewModel by sharedViewModel<ChartsViewModel>()

    private val itemId by lazy { requireArguments().getString(ID_KEY, "") }
    private val itemType by lazy { requireArguments().getString(TYPE_KEY, "") }
    private val isCurrency by lazy { requireArguments().getBoolean(IS_CURRENCY_KEY, false) }

    private lateinit var binding: FragmentHistoryBinding

    private val picassoInstance = Picasso.get()

    internal val formatter = SimpleDateFormat("MMM dd", Locale.getDefault())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding = FragmentHistoryBinding.bind(view)
        binding = getBinding()

        val progressDialog = requireActivity().getTransparentProgressDialog()

        if (itemType.isEmpty() || itemId.isEmpty()) {
            router.exit()
            return
        }

        lifecycleScope.launchWhenResumed {
            val data = if (isCurrency) {
                viewModel.getCurrencyHistory(settings.league, itemType, itemId)
            } else {
                viewModel.getItemHistory(settings.league, itemType, itemId)
            }
            val textColor = ContextCompat.getColor(
                requireActivity(),
                R.color.primaryTextColor
            )
            val textFont = ResourcesCompat.getFont(
                requireActivity(),
                com.poe.tradeapp.core.R.font.fontinsmallcaps
            )
            setupChartData(data.buyingGraphData, false, textColor, textFont)
            setupChartData(data.sellingGraphData, true, textColor, textFont)
            setupChart(data, textColor, textFont)
            binding.toolbarLayout.toolbar.title = "Item history"
            binding.itemLabel.text = data.name
            binding.goToWiki.setOnClickListener {
                createWikiDialog(data.name).show()
            }
            binding.buyButton.setOnClickListener {
                getMainActivity()?.goToCurrencyExchange(data.tradeId, "chaos")
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
                picassoInstance.load(data.icon).fit().into(binding.leftSideSellImage)
            }
            picassoInstance.load(data.icon).fit().into(binding.leftSideBuyImage)
            picassoInstance.load(data.icon).fit().into(binding.itemImage)
        }
        lifecycleScope.launchWhenCreated {
            viewModel.viewLoadingState.collect {
                if (it) {
                    progressDialog.show()
                } else {
                    progressDialog.dismiss()
                }
            }
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
            if (selling) R.color.primaryLineChartColor else R.color.secondaryLineChartColor
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
        binding.chart.xAxis.apply {
            axisMinimum = 0f
            axisMaximum = xMax
            position = XAxis.XAxisPosition.BOTTOM
            labelRotationAngle = -45f
            textSize = 16f
            typeface = textFont
            this.textColor = textColor
            valueFormatter = object : ValueFormatter() {
                override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                    val currentDate = Calendar.getInstance()
                    currentDate.add(Calendar.DAY_OF_YEAR, -(xMax - value).toInt())
                    return formatter.format(currentDate.time)
                }
            }
        }
        binding.chart.axisLeft.apply {
            textSize = 16f
            typeface = textFont
            this.textColor = textColor
            xOffset = 15f
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
            this.invalidate()
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
        private const val ID_KEY = "ID_KEY"
        private const val TYPE_KEY = "TYPE_KEY"
        private const val IS_CURRENCY_KEY = "IS_CURRENCY_KEY"

        fun newInstance(id: String, type: String, isCurrency: Boolean): FragmentScreen {
            return FragmentScreen {
                HistoryFragment().apply {
                    arguments = bundleOf(
                        ID_KEY to id,
                        TYPE_KEY to type,
                        IS_CURRENCY_KEY to isCurrency
                    )
                }
            }
        }
    }
}