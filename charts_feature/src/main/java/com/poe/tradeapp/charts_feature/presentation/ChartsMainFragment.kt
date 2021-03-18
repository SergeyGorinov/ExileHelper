package com.poe.tradeapp.charts_feature.presentation

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.poe.tradeapp.charts_feature.R
import com.poe.tradeapp.charts_feature.databinding.FragmentChartsMainBinding
import com.poe.tradeapp.charts_feature.presentation.adapters.ItemsGroupsAdapter
import com.poe.tradeapp.core.presentation.BaseFragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.*

class ChartsMainFragment : BaseFragment(R.layout.fragment_charts_main) {

    private val viewModel by sharedViewModel<ChartsViewModel>()

    private lateinit var binding: FragmentChartsMainBinding

//    internal val formatter = SimpleDateFormat("MMM dd", Locale.getDefault())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentChartsMainBinding.bind(view)
        binding = getBinding()

        binding.toolbarLayout.toolbar.title = "Select items group"
        binding.groups.addItemDecoration(
            DividerItemDecoration(
                requireActivity(),
                DividerItemDecoration.VERTICAL
            ).apply {
                ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.recyclerview_divider
                )?.let {
                    setDrawable(it)
                }
            })
        binding.groups.layoutManager = LinearLayoutManager(requireActivity())
        binding.groups.adapter = ItemsGroupsAdapter(viewModel.getItemsGroups()) {
            return@ItemsGroupsAdapter
        }

//        lifecycleScope.launchWhenResumed {
//            val data = viewModel.getCurrencyHistory("Ritual", "Currency", "2").apply {
//                receiveCurrencyGraphData.apply {
//                    val currentColor = ContextCompat.getColor(
//                        requireActivity(),
//                        R.color.primaryLineChartColor
//                    )
//                    color = currentColor
//                    setCircleColor(currentColor)
//                }
//                payCurrencyGraphData.apply {
//                    val currentColor = ContextCompat.getColor(
//                        requireActivity(),
//                        R.color.secondaryLineChartColor
//                    )
//                    color = currentColor
//                    setCircleColor(currentColor)
//                }
//            }
//            val xMax = maxOf(data.payCurrencyGraphData.xMax, data.receiveCurrencyGraphData.xMax)
//            binding.chart.xAxis.apply {
//                axisMinimum = 0f
//                axisMaximum = xMax
//                position = XAxis.XAxisPosition.BOTTOM
//                labelCount = 10
//                labelRotationAngle = -45f
//                textSize = 16f
//                textColor = ContextCompat.getColor(requireActivity(), R.color.primaryTextColor)
//                valueFormatter = object : ValueFormatter() {
//                    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
//                        val currentDate = Calendar.getInstance()
//                        currentDate.add(Calendar.DAY_OF_YEAR, -(xMax - value).toInt())
//                        return formatter.format(currentDate.time)
//                    }
//                }
//            }
//            binding.chart.axisLeft.apply {
//                textSize = 16f
//                textColor = ContextCompat.getColor(requireActivity(), R.color.primaryTextColor)
//                xOffset = 10f
//            }
//            binding.chart.apply {
//                marker = CustomMarkerView(requireActivity(), R.layout.custom_marker_view)
//                description.isEnabled = false
//                legend.isEnabled = false
//                axisRight.isEnabled = false
//                setTouchEnabled(true)
//                setVisibleXRangeMaximum(10f)
//                this.data = LineData(data.receiveCurrencyGraphData, data.payCurrencyGraphData)
//                this.invalidate()
//            }
//        }
    }

//    private class CustomMarkerView(context: Context, resId: Int) : MarkerView(context, resId) {
//
//        override fun refreshContent(e: Entry?, highlight: Highlight?) {
//            e?.y?.let {
//                findViewById<TextView>(R.id.tvContent).text = String.format("%.1f", it)
//            }
//            super.refreshContent(e, highlight)
//        }
//
//        override fun getOffset(): MPPointF {
//            val xOffset = -(width / 2)
//            val yOffset = -height - 10
//            return MPPointF(xOffset.toFloat(), yOffset.toFloat())
//        }
//    }

    companion object {
        fun newInstance() = FragmentScreen { ChartsMainFragment() }
    }
}
