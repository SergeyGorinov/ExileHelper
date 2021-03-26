package com.poe.tradeapp.exchange.presentation.fragments

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.poe.tradeapp.core.presentation.BaseFragment
import com.poe.tradeapp.core.presentation.CenteredImageSpan
import com.poe.tradeapp.exchange.R
import com.poe.tradeapp.exchange.data.models.ResponseModel
import com.poe.tradeapp.exchange.databinding.FragmentResultBinding
import com.poe.tradeapp.exchange.presentation.ItemsSearchViewModel
import com.poe.tradeapp.exchange.presentation.SeparatorHelper
import com.poe.tradeapp.exchange.presentation.adapters.ItemsResultAdapter
import com.poe.tradeapp.exchange.presentation.models.FetchedItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ItemsSearchResultFragment : BaseFragment(R.layout.fragment_result) {

    private val viewModel by sharedViewModel<ItemsSearchViewModel>()

    private lateinit var binding: FragmentResultBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding = FragmentResultBinding.inflate(layoutInflater)
        binding = getBinding()

        val layoutManager = LinearLayoutManager(context)
        val adapter = ItemsResultAdapter()

        adapter.setHasStableIds(true)

        binding.results.layoutManager = layoutManager
        binding.results.adapter = adapter
        binding.results.setHasFixedSize(true)
        binding.results.setItemViewCacheSize(100)
        binding.results.scrollToPosition(0)

        binding.results.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            private var isLoading = false

            private var totalCount = viewModel.itemsResultData?.result?.size ?: 0

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalCurrentItems = layoutManager.itemCount
                if (totalCurrentItems == totalCount)
                    return
                val lastVisiblePosition = layoutManager.findLastVisibleItemPosition()
                if (!isLoading && (totalCurrentItems - 1) == lastVisiblePosition) {
                    isLoading = true
                    recyclerView.post {
                        adapter.addLoader()
                    }
                    lifecycleScope.launch {
                        withContext(Dispatchers.IO) {
                            viewModel.fetchPartialResults(totalCurrentItems)
                        }
                        isLoading = false
                    }
                }
            }
        })

//        viewModel.responseItems.onEach {
//            adapter.addFetchedItems(populateResponse(it))
//        }.launchIn(lifecycleScope)
    }

    private fun populateResponse(items: List<ResponseModel>): List<FetchedItem> {
        var isOddRow = true
        return items.map { item ->
            var hybridItemText: SpannableStringBuilder? = null

            val currentItem = item.item
            val itemText = SpannableStringBuilder()
            val colorId =
                if (isOddRow) R.color.odd_result_row_color
                else R.color.even_result_row_color

            isOddRow = !isOddRow

            listOfNotNull(
                SeparatorHelper.getSpannableTextGroup(currentItem.properties, "\n"),
                SeparatorHelper.getSpannableTextGroup(currentItem.requirements, ", "),
                currentItem.secDescrText,
                currentItem.implicitMods,
                currentItem.explicitMods,
                currentItem.note
            ).forEach {
                populateItemText(it, itemText, currentItem.frameType)
            }

            if (currentItem.hybrid != null) {
                hybridItemText = SpannableStringBuilder()

                listOfNotNull(
                    SeparatorHelper.getSpannableTextGroup(currentItem.hybrid.properties, "\n"),
                    SeparatorHelper.getSpannableTextGroup(currentItem.hybrid.requirements, ", "),
                    currentItem.hybrid.secDescrText,
                    currentItem.hybrid.implicitMods,
                    currentItem.hybrid.explicitMods
                ).forEach {
                    populateItemText(it, hybridItemText, currentItem.frameType)
                }
            }

            FetchedItem(
                item.item.name,
                item.item.typeLine,
                item.item.icon,
                item.item.sockets,
                currentItem.frameType,
                colorId,
                SeparatorHelper.getInfluenceIcons(item.item),
                itemText,
                currentItem.hybrid?.baseTypeName,
                hybridItemText
            )
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun populateItemText(
        properties: Any,
        itemText: SpannableStringBuilder,
        frameType: Int?
    ) {
        if (itemText.isNotEmpty())
            itemText.insertSeparator(SeparatorHelper.getSeparator(frameType))
        when (properties) {
            is List<*> -> {
                itemText.append(properties.joinToString("\n"))
            }
            is String -> {
                itemText.append(properties)
            }
            is SpannableStringBuilder -> {
                itemText.append(properties)
            }
        }
    }

    private fun SpannableStringBuilder.insertSeparator(image: Int) {
        this.appendLine()
        this.append(' ')
        this.setSpan(
            CenteredImageSpan(requireContext(), image),
            this.length - 1,
            this.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        this.appendLine()
    }
}